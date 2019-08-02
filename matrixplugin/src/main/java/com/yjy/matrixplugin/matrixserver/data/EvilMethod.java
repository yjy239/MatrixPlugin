package com.yjy.matrixplugin.matrixserver.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/07/31
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EvilMethod implements Parcelable {

    /**
     * machine : HIGH
     * cpu_app : 5.9163536634931136E-5
     * mem : 3901747200
     * mem_free : 107436
     * detail : NORMAL
     * cost : 1229
     * usage : 0.24%
     * stack : 0,1048574,1,1229
     1,520,1,1221
     2,522,1,1221
     3,523,1,382
     4,524,1,161
     5,525,1,21
     5,526,1,20
     5,527,1,21
     4,528,1,21
     3,529,1,16
     3,530,1,16
     3,531,1,5
     * stackKey : 522|
     * tag : Trace_EvilMethod
     * process : com.yjy.matrixplugin
     * time : 1564645619303
     */

    private String machine;
    private double cpu_app;
    private long mem;
    private int mem_free;
    private String detail;
    private int cost;
    private String usage;
    private String stack;
    private String stackKey;
    private String tag;
    private String process;
    private long time;

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public double getCpu_app() {
        return cpu_app;
    }

    public void setCpu_app(double cpu_app) {
        this.cpu_app = cpu_app;
    }

    public long getMem() {
        return mem;
    }

    public void setMem(long mem) {
        this.mem = mem;
    }

    public int getMem_free() {
        return mem_free;
    }

    public void setMem_free(int mem_free) {
        this.mem_free = mem_free;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getStackKey() {
        return stackKey;
    }

    public void setStackKey(String stackKey) {
        this.stackKey = stackKey;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.machine);
        dest.writeDouble(this.cpu_app);
        dest.writeLong(this.mem);
        dest.writeInt(this.mem_free);
        dest.writeString(this.detail);
        dest.writeInt(this.cost);
        dest.writeString(this.usage);
        dest.writeString(this.stack);
        dest.writeString(this.stackKey);
        dest.writeString(this.tag);
        dest.writeString(this.process);
        dest.writeLong(this.time);
    }

    public EvilMethod() {
    }

    protected EvilMethod(Parcel in) {
        this.machine = in.readString();
        this.cpu_app = in.readDouble();
        this.mem = in.readLong();
        this.mem_free = in.readInt();
        this.detail = in.readString();
        this.cost = in.readInt();
        this.usage = in.readString();
        this.stack = in.readString();
        this.stackKey = in.readString();
        this.tag = in.readString();
        this.process = in.readString();
        this.time = in.readLong();
    }

    public static final Creator<EvilMethod> CREATOR = new Creator<EvilMethod>() {
        @Override
        public EvilMethod createFromParcel(Parcel source) {
            return new EvilMethod(source);
        }

        @Override
        public EvilMethod[] newArray(int size) {
            return new EvilMethod[size];
        }
    };
}
