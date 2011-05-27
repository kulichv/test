// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.controllers.license.impl;

import org.gridgain.grid.kernal.controllers.license.*;
import org.gridgain.grid.typedef.internal.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;
import java.util.*;

/**
 * GridGain license version 2 bean.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@XmlRootElement(name = "gridgain-license")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={
    "id",
    "issueDate",
    "issueOrganization",
    "userOrganization",
    "licenseNote",
    "userWww",
    "userEmail",
    "userName",
    "versionRegexp",
    "type",
    "meteringKey1",
    "meteringKey2",
    "expireDate",
    "maxNodes",
    "maxCpus",
    "maxComputers",
    "gracePeriod",
    "maxUpTime",
    "signature",
    "disabledSubsystems"
})
public class GridLicenseV2Adapter implements GridLicenseV2 {
    /** License version. */
    private String ver;

    /** License ID. */
    private UUID id;

    /** Maximum up time in minutes. */
    private long maxUpTime;

    /** Grace period in minutes. */
    private long gracePeriod;

    /** The date the license was generated/issued. */
    private Date issueDate;

    /** Organization that issued the license. */
    private String issueOrg;

    /** Organization the license was issued to. */
    private String userOrg;

    /** URL of organization the license was issued to. */
    private String userWww;

    /** Name of the contact for organization the license was issued to. */
    private String userName;

    /** License note. */
    private String licNote;

    /** Version regular expression. If empty - no restrictions. */
    private String verRegexp;

    /** Email of organization the license was issued to. */
    private String userEmail;

    /** License type. */
    private GridLicenseType type;

    /**  Security key 1 for metering. */
    private String meterKey1;

    /** Security key 2 for metering. */
    private String meterKey2;

    /** Expiration date of the license. If empty - never expires. */
    private Date expDate;

    /** Disabled subsystems. */
    private String disSubs;

    /**
     * Maximum number of nodes in the topology allowed.
     * If zero - no restriction.
     */
    private int maxNodes;

    /**
     * Maximum number of CPUs in the topology allowed.
     * If zero - no restriction.
     */
    private int maxCpus;

    /**
     * Maximum number of physical computers in the topology allowed.
     * If zero - no restrictions.
     */
    private int maxComp;

    /**
     * Digest calculated based on all fields to enforce the integrity of the license.
     */
    private String sign;

    /** {@inheritDoc} */
    @XmlAttribute(name = "version")
    @Override public String getVersion() {
        return ver;
    }

    /**
     * Sets license version.
     *
     * @param ver License version.
     */
    public void setVersion(String ver) {
        this.ver = ver;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "version-regexp")
    @Override public String getVersionRegexp() {
        return verRegexp;
    }

    /**
     * Sets comma separated list of disabled subsystems.
     *
     * @param disSubs Comma separated list of disabled subsystems.
     */
    public void setDisabledSubsystems(String disSubs) {
        this.disSubs = disSubs;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "disabled-subsystems")
    @Override public String getDisabledSubsystems() {
        return disSubs;
    }

    /**
     * Sets license regular expression.
     *
     * @param verRegexp License version regular expression.
     */
    public void setVersionRegexp(String verRegexp) {
        this.verRegexp = verRegexp;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "id")
    @Override public UUID getId() {
        return id;
    }

    /**
     * Sets license ID.
     *
     * @param id License ID.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"ReturnOfDateField"})
    @XmlElement(name = "issue-date")
    @XmlJavaTypeAdapter(GridLicenseDateAdapter.class)
    @Override public Date getIssueDate() {
        return issueDate;
    }

    /**
     * Sets issue date.
     *
     * @param issueDate Issue date.
     */
    @SuppressWarnings({"AssignmentToDateFieldFromParameter"})
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "issue-org")
    @Override public String getIssueOrganization() {
        return issueOrg;
    }

    /**
     * Sets issue organization.
     *
     * @param issueOrg Issue organization.
     */
    public void setIssueOrganization(String issueOrg) {
        this.issueOrg = issueOrg;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "user-org")
    @Override public String getUserOrganization() {
        return userOrg;
    }

    /**
     * Sets user organization.
     *
     * @param userOrg User organization.
     */
    public void setUserOrganization(String userOrg) {
        this.userOrg = userOrg;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "user-www")
    @Override public String getUserWww() {
        return userWww;
    }

    /**
     * Sets user name.
     *
     * @param userName User name.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "user-name")
    @Override public String getUserName() {
        return userName;
    }

    /**
     * Sets user organization URL.
     *
     * @param userWww User organization URL.
     */
    public void setUserWww(String userWww) {
        this.userWww = userWww;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "user-email")
    @Override public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets license note.
     *
     * @param licNote License note.
     */
    public void setLicenseNote(String licNote) {
        this.licNote = licNote;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "license-note")
    @Override public String getLicenseNote() {
        return licNote;
    }

    /**
     * Sets user organization e-mail.
     *
     * @param userEmail User organization e-mail.
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "type")
    @Override public GridLicenseType getType() {
        return type;
    }

    /**
     * Sets license type.
     *
     * @param type License type.
     */
    public void setType(GridLicenseType type) {
        this.type = type;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "meter-key1")
    @Override public String getMeteringKey1() {
        return meterKey1;
    }

    /**
     * Sets metering key 1.
     *
     * @param meterKey1 Metering key 1.
     */
    public void setMeteringKey1(String meterKey1) {
        this.meterKey1 = meterKey1;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "meter-key2")
    @Override public String getMeteringKey2() {
        return meterKey2;
    }

    /**
     * Sets metering key 2.
     *
     * @param meterKey2 Metering key 2.
     */
    public void setMeteringKey2(String meterKey2) {
        this.meterKey2 = meterKey2;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"ReturnOfDateField"})
    @XmlElement(name = "expire-date")
    @XmlJavaTypeAdapter(GridLicenseDateAdapter.class)
    @Override public Date getExpireDate() {
        return expDate;
    }

    /**
     * Sets expire date.
     *
     * @param expDate Expire date.
     */
    @SuppressWarnings({"AssignmentToDateFieldFromParameter"})
    public void setExpireDate(Date expDate) {
        this.expDate = expDate;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "max-nodes")
    @Override public int getMaxNodes() {
        return maxNodes;
    }

    /**
     * Sets maximum number of nodes.
     *
     * @param maxNodes Maximum number of nodes.
     */
    public void setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "max-computers")
    @Override public int getMaxComputers() {
        return maxComp;
    }

    /**
     * Sets maximum number of computers.
     *
     * @param maxComp Maximum number of computers.
     */
    public void setMaxComputers(int maxComp) {
        this.maxComp = maxComp;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "max-cpus")
    @Override public int getMaxCpus() {
        return maxCpus;
    }

    /**
     * Sets maximum number of CPUs.
     *
     * @param maxCpus Maximum number of CPUs.
     */
    public void setMaxCpus(int maxCpus) {
        this.maxCpus = maxCpus;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "signature")
    @Override public String getSignature() {
        return sign;
    }

    /**
     * Sets signature.
     *
     * @param sign Signature.
     */
    public void setSignature(String sign) {
        this.sign = sign;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "max-uptime")
    @Override public long getMaxUpTime() {
        return maxUpTime;
    }

    /**
     * Sets maximum up time in minutes.
     *
     * @param maxUpTime Maximum up time in minutes.
     */
    public void setMaxUpTime(long maxUpTime) {
        this.maxUpTime = maxUpTime;
    }

    /** {@inheritDoc} */
    @XmlElement(name = "grace-period")
    @Override public long getGracePeriod() {
        return gracePeriod;
    }

    /**
     * Sets grace period in minutes.
     *
     * @param gracePeriod Grace period in minutes.
     */
    public void setGracePeriod(long gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridLicenseV2Adapter.class, this);
    }
}
