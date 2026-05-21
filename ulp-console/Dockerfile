#
# ulp-console - United Login Platform
# Copyright (c) 2022-Present Frank Zhang
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM azul/zulu-openjdk:17-jre as build
WORKDIR /workspace/app

ARG JAR_FILE=target/topiam-employee-console-*.jar
COPY ${JAR_FILE} target/application.jar
RUN java -Djarmode=layertools -jar target/application.jar extract --destination target/extracted

FROM azul/zulu-openjdk:17-jre

ARG EXTRACTED=/workspace/app/target/extracted
WORKDIR topiam
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./


ENV TZ=Asia/Shanghai
RUN sed -i 's/archive.ubuntu.com/cn.archive.ubuntu.com/g' /etc/apt/sources.list \
          && apt-get update \
          && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
          && apt-get clean \
          && apt-get autoclean \
          && apt-get autoremove -y \
          && apt-get install -y curl \
          && apt-get install -y iputils-ping \
          && apt-get install -y wget \
          && apt-get install -y tzdata \
          && apt-get install -y fontconfig \
          && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* \

EXPOSE 1898

ENTRYPOINT ["java","-XX:TieredStopAtLevel=1","-Djava.security.egd=file:/dev/./urandom","-Dspring.main.lazy-initialization=false","org.springframework.boot.loader.launch.JarLauncher"]