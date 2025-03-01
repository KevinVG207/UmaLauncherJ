package com.kevinvg.umalauncherj.util;

import com.cronutils.utils.StringUtils;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

@EqualsAndHashCode
public class Version implements Comparable<Version>{
    private int[] versionArray = {0,0,0};

    public Version(){}

    public Version(String versionString){
        String[] nums = versionString.split("\\.");
        if (nums.length != 3){
            throw new RuntimeException("Version format error " + versionString);
        }

        this.versionArray = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            this.versionArray[i] = Integer.parseInt(nums[i]);
        }
    }

    public Version(int[] versionArray){
        this.versionArray = Arrays.copyOf(versionArray, versionArray.length);
    }

    @Override
    public int compareTo(Version o) {
        return Arrays.compare(this.versionArray, o.versionArray);
    }

    @Override
    public String toString() {
        return StringUtils.join(ArrayUtils.toObject(versionArray), ".");
    }
}
