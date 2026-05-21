/*
 * ulp-core - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.core.message.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cn.frank.ulp.common.entity.message.MailSendRecordEntity;
import cn.frank.ulp.common.entity.setting.MailTemplateEntity;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.exception.MailMessageSendException;
import cn.frank.ulp.common.message.mail.MailProviderSend;
import cn.frank.ulp.common.message.mail.SendMailRequest;
import cn.frank.ulp.common.repository.message.MailSendRecordRepository;
import cn.frank.ulp.core.message.MsgVariable;
import cn.frank.ulp.core.setting.MessageSettingConstants;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.springframework.web.util.HtmlUtils.htmlUnescape;

import static cn.frank.ulp.support.constant.EiamConstants.COLON;

/**
 * 消息通知事件
 *
 * @author Frank Zhang
 */
@Async
@Component
public class MailMsgEventListener implements ApplicationListener<MailMsgEvent> {
    private final Logger logger = LoggerFactory.getLogger(MailMsgEventListener.class);

    @Override
    public void onApplicationEvent(@NonNull MailMsgEvent event) {
        // 邮件通知类型
        MailType type = event.getType();
        String content = htmlUnescape(MailUtils.readEmailContent(type.getContent()));
        String subject = type.getSubject();
        String sender = type.getSender();
        try {
            Map<String, Object> parameter = event.getParameter();
            // 判断是否存在自定义
            MailTemplateEntity templateEntity = (MailTemplateEntity) redisTemplate.opsForValue()
                .get(MessageSettingConstants.SETTING_EMAIL_TEMPLATE_CACHE_NAME + COLON
                     + type.getCode());
            if (!Objects.isNull(templateEntity)) {
                content = htmlUnescape(templateEntity.getContent());
                subject = templateEntity.getSubject();
                sender = templateEntity.getSender();
            }
            // 主题
            StringWriter themeStringWriter = new StringWriter();
            Template themeTpl = createTemplate(type.getCode() + "-theme", subject);
            themeTpl.process(parameter, themeStringWriter);
            // 测试邮件
            if (parameter.containsKey(MsgVariable.TEST)) {
                String test = parameter.get(MsgVariable.TEST).toString();
                themeStringWriter.append(test);
            }
            // 内容
            StringWriter contentStringWriter = new StringWriter();
            Template contentTpl = createTemplate(type.getCode() + "-content", content);
            contentTpl.process(parameter, contentStringWriter);
            // 发送邮件
            //@formatter:off
            SendMailRequest params = new SendMailRequest()
                    //  发送人
                    .setSender(sender)
                    // 接收人
                    .setReceiver(event.getReceiver())
                    // 主题
                    .setSubject(themeStringWriter.toString())
                    // 内容
                    .setBody(contentStringWriter.toString());
            //@formatter:on
            // 发送邮件
            mailProviderSend.sendMailHtml(params);
            // 保存发送记录
            MailSendRecordEntity record = new MailSendRecordEntity();
            //@formatter:off
            record.setSender(event.getReceiver())
                    .setSubject(params.getSubject())
                    .setContent(params.getBody())
                    .setProvider(mailProviderSend.getProvider())
                    .setType(event.getType())
                    .setReceiver(event.getReceiver())
                    .setSendTime(LocalDateTime.now())
                    .setSuccess(true);
            //@formatter:on
            mailSendRecordRepository.save(record);
        } catch (Exception e) {
            logger.error("邮件信息发送失败: {}", e.getMessage());
            throw new MailMessageSendException("邮件信息发送失败！", e);
        }
    }

    /**
     * 创建模板
     *
     * @param name            {@link String} 模板名称
     * @param templateContent {@link String} 模板内容
     * @return {@link Template}
     * @throws IOException IOException
     */
    private Template createTemplate(String name, String templateContent) throws IOException {
        try {
            Template template = freeMarkerConfiguration.getTemplate(name, UTF_8);
            if (template != null) {
                if (template.toString().trim().equals(templateContent.trim())) {
                    return template;
                }
            }
        } catch (Exception ignored) {
        }
        // 以下操作不是线程安全，要加上同步
        synchronized (this) {
            // 获取模板加载器
            TemplateLoader templateLoader = freeMarkerConfiguration.getTemplateLoader();
            if (templateLoader instanceof StringTemplateLoader) {
                // 如果加载器已经是字符串加载器，则在原来的加载器上put一个新的模板
                ((StringTemplateLoader) templateLoader).putTemplate(name, templateContent);
                freeMarkerConfiguration.setTemplateLoader(templateLoader);
            } else {
                // 如果原来的模板加载器不是字符串的（默认是文件加载器），则新建
                StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
                stringTemplateLoader.putTemplate(name, templateContent);
                freeMarkerConfiguration.setTemplateLoader(stringTemplateLoader);
            }
            // 这里要清一下缓存，不然下面可能会获取不到模板
            freeMarkerConfiguration.clearTemplateCache();

            return freeMarkerConfiguration.getTemplate(name, UTF_8);
        }
    }

    /**
     * MailSend
     */
    private final MailProviderSend              mailProviderSend;

    /**
     * MailSendRecordRepository
     */
    private final MailSendRecordRepository      mailSendRecordRepository;
    /**
     * redis
     */
    private final RedisTemplate<Object, Object> redisTemplate;

    private final Configuration                 freeMarkerConfiguration;

    public MailMsgEventListener(MailProviderSend mailProviderSend,
                                MailSendRecordRepository mailSendRecordRepository,
                                RedisTemplate<Object, Object> redisTemplate) {
        this.mailProviderSend = mailProviderSend;
        this.mailSendRecordRepository = mailSendRecordRepository;
        this.redisTemplate = redisTemplate;
        freeMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_31);
        //设置模板加载文件夹
        freeMarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/mail/content");
    }

}
