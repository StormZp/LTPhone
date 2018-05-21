package com.netphone.netsdk.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:42<p>
 * Message {最后一条数据bean}
 */
@Entity
public class LastPositionBean implements Serializable {
    /**
     * ID : 18041717250233320423
     * UserID : 17090410015273146616
     * TerminalID : 2620093865
     * Latitude : 37.42823866141594
     * Longitude : -122.07740940828037
     * UploadDateTime : 2018-04-17 17:25:02
     * OrganizeId : 17080216141881996
     * PositionAddress : North Shoreline Boulevard, Mountain View, California, United States()
     */
    static final long serialVersionUID = 42L;

    private String ID;
    @Id
    private String UserID;
    private String TerminalID;
    private double Latitude;
    private double Longitude;
    private String UploadDateTime;
    private String OrganizeId;
    private String PositionAddress;

    @Generated(hash = 1260719977)
    public LastPositionBean(String ID, String UserID, String TerminalID, double Latitude,
            double Longitude, String UploadDateTime, String OrganizeId,
            String PositionAddress) {
        this.ID = ID;
        this.UserID = UserID;
        this.TerminalID = TerminalID;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.UploadDateTime = UploadDateTime;
        this.OrganizeId = OrganizeId;
        this.PositionAddress = PositionAddress;
    }

    @Generated(hash = 36931912)
    public LastPositionBean() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUploadDateTime() {
        return UploadDateTime;
    }

    public void setUploadDateTime(String uploadDateTime) {
        UploadDateTime = uploadDateTime;
    }

    public String getOrganizeId() {
        return OrganizeId;
    }

    public void setOrganizeId(String organizeId) {
        OrganizeId = organizeId;
    }

    public String getPositionAddress() {
        return PositionAddress;
    }

    public void setPositionAddress(String positionAddress) {
        PositionAddress = positionAddress;
    }
}
