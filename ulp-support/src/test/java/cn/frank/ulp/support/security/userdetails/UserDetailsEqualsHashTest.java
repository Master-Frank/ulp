/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.userdetails;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 暴露 UserDetails.equals/hashCode 契约违反：
 * - equals 只比较 username
 * - hashCode 把 email 算进去
 * 同 username 不同 email 时 equals=true 但 hashCode 不同 → Java 契约违反，
 * 影响 HashMap/HashSet/Spring SessionRegistry 的 key 行为。
 */
class UserDetailsEqualsHashTest {

    private static UserDetails newUser(String username, String email) {
        UserDetails u = new UserDetails("id-" + username, username, "pwd",
            new UserType("USER", "User"), true, true, true, true, Collections.emptyList());
        u.setEmail(email);
        return u;
    }

    @Test
    void sameUsernameSameEmailEquals() {
        UserDetails a = newUser("alice", "a@x.com");
        UserDetails b = newUser("alice", "a@x.com");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    /**
     * 当前实现下：equals=true 但 hashCode 不同 — 暴露契约违反。
     * 此测试应该失败 — 失败本身就是 bug 报告。
     * 修复后应改为 assertThat(a.hashCode()).isEqualTo(b.hashCode())。
     */
    @Test
    void sameUsernameDifferentEmailHashCodeContract() {
        UserDetails a = newUser("alice", "old@x.com");
        UserDetails b = newUser("alice", "new@x.com");

        // 实际行为：equals 返回 true（只比 username）
        assertThat(a).isEqualTo(b);

        // Java 契约：equals true ⇒ hashCode 必须相等
        assertThat(a.hashCode())
            .as("equals/hashCode contract violation: same username different email")
            .isEqualTo(b.hashCode());
    }

    @Test
    void differentUsernameNotEquals() {
        UserDetails a = newUser("alice", "a@x.com");
        UserDetails b = newUser("bob", "a@x.com");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void usableInHashSet() {
        UserDetails a = newUser("alice", "old@x.com");
        UserDetails b = newUser("alice", "new@x.com");
        // 当前实现下：HashSet.contains(b) 可能返回 false 即使 a.equals(b) = true
        // 因为 hashCode 不同会落到不同 bucket
        java.util.Set<UserDetails> set = new java.util.HashSet<>(List.of(a));
        assertThat(set.contains(b))
            .as("HashSet.contains broken when equals/hashCode contract violated").isTrue();
    }
}
