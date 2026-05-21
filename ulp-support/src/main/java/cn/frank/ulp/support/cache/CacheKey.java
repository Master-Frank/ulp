/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.cache;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class CacheKey implements Serializable {

    private final String   group;

    private final Object[] params;

    private final int      hashCodeValue;

    private final String   keyName;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheKey)) {
            return false;
        }
        CacheKey other = (CacheKey) obj;
        return this.group.equals(other.group) && this.keyName.equals(other.keyName)
               && Arrays.deepEquals(this.params, other.params);
    }

    public CacheKey(String group, String keyName, Object... params) {
        Assert.notNull(group, "group must not be null");
        Assert.notNull(params, "params must not be null");
        this.group = group;
        this.keyName = keyName;
        this.params = new Object[params.length];
        System.arraycopy(params, 0, this.params, 0, params.length);

        int hash = this.group.hashCode();
        hash = 31 * hash + this.keyName.hashCode();
        hash = 31 * hash + Arrays.deepHashCode(this.params);
        this.hashCodeValue = hash;
    }

    public String toString() {
        return this.group + " " + this.getClass().getSimpleName() + this.keyName + " ["
               + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
    }

    public final int hashCode() {
        return this.hashCodeValue;
    }
}
