/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具.
 */
public class Pinyin4jUtils {

    private static final String CHINESE_REGEX = "[\u4E00-\u9FA5]+";

    public Pinyin4jUtils() {
    }

    public static Set<String> getPinyin(String input, boolean fullPinyin) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        char[] chars = input.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String[][] cells = new String[chars.length][];
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (String.valueOf(c).matches(CHINESE_REGEX)) {
                try {
                    cells[i] = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (!fullPinyin && i != 0 && cells[i] != null) {
                        String[] firsts = new String[cells[i].length];
                        for (int j = 0; j < cells[i].length; j++) {
                            firsts[j] = Character.toString(cells[i][j].charAt(0));
                        }
                        cells[i] = firsts;
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    throw new RuntimeException(e);
                }
            } else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                cells[i] = new String[] { String.valueOf(c) };
            } else {
                cells[i] = new String[] { "" };
            }
        }
        String[] combos = exchange(cells);
        return new HashSet<>(Arrays.asList(combos));
    }

    public static String getFirstSpellPinYin(String input, boolean fullPinyin) {
        Set<String> pinyins = getPinyin(input, fullPinyin);
        if (pinyins == null) {
            return null;
        }
        String joined = makeStringByStringSet(pinyins);
        String[] split = joined.split(",");
        return split.length > 1 ? split[0] : joined;
    }

    public static String getPinYinHeadChar(String input) {
        StringBuilder sb = new StringBuilder();
        if (input == null) {
            return "";
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            String[] arr = PinyinHelper.toHanyuPinyinStringArray(c);
            if (arr != null && arr.length > 0) {
                sb.append(arr[0].charAt(0));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String[] exchange(String[][] cells) {
        if (cells == null || cells.length == 0) {
            return new String[] { "" };
        }
        // 笛卡尔积合成
        List<String> current = new ArrayList<>();
        current.add("");
        for (String[] cell : cells) {
            if (cell == null || cell.length == 0) {
                continue;
            }
            List<String> next = new ArrayList<>(current.size() * cell.length);
            for (String prefix : current) {
                for (String s : cell) {
                    next.add(prefix + s);
                }
            }
            current = next;
        }
        return current.toArray(new String[0]);
    }

    public static String makeStringByStringSet(Set<String> set) {
        if (set == null || set.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int last = set.size() - 1;
        for (String s : set) {
            sb.append(s);
            if (i != last) {
                sb.append(",");
            }
            i++;
        }
        return sb.toString().toLowerCase();
    }
}
