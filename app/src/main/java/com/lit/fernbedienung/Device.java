package com.lit.fernbedienung;

public class Device
{
    private char fam;
    private int group;
    private int device;
    private String name;

    public Device(char pFam, int pGruop, int pDevice, String pName)
    {
        fam = pFam;
        group = pGruop;
        device = pDevice;
        name = pName;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public char getFam()
    {
        return fam;
    }

    public void setFam(char fam)
    {
        this.fam = fam;
    }

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }

    public int getDevice()
    {
        return device;
    }

    public void setDevice(int device)
    {
        this.device = device;
    }

    public boolean isValid()
    {
        return String.valueOf(fam).length()==1 && String.valueOf(group).length()==1 && String.valueOf(device).length()==1;
    }
}
