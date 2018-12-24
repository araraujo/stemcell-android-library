package com.stemcell.android.beans;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Value Object of Transporting User Data Security
 *
 */
public class CredentialsBean implements Serializable {
    /**
     * Stores the key of an authenticated user
     */
    private String logon;
    /**
     * Stores the name of an authenticated user
     */
    private String username;
    /**
     * Stores the access control resources allowed to the user in order to transport between server
     * and remote client
     */
    private Set<String> securityResources;

    /**
     * Construtor
     */
    public CredentialsBean() {
    }

    /**
     * Construtor
     * @param logon user key
     * @param username User Name
     * @param securityResources security resources
     */
    public CredentialsBean(String logon, String username, Set securityResources) {
        this.logon = logon;
        this.username = username;
        this.securityResources = new HashSet<String>(securityResources);
    }

    /**
     * @return User login (key)
     */
    public String getLogon() {
        return logon;
    }

    public void setLogon(String logon) {
        this.logon = logon;
    }

    /**
     * @return Username logged in
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return true if you are authenticated
     */
    public boolean isAuthenticated() {
        return username != null;
    }

    /**
     * @return List of user security resources
     */
    public Set<String> getSecurityResources() {
        return Collections.unmodifiableSet(securityResources);
    }

    public void setSecurityResources(Set<String> securityResources) {
        this.securityResources = securityResources;
    }

    /**
     * Checks if any resource exists for the user
     * @param asList list of resources
     * @return true if any resource exists for the user
     */
    public boolean verifyAnySecurityResource(Collection<String> asList) {
        for (String obj : asList) {
            if (securityResources.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies the resource exists for the user
     * @param resource resource
     * @return true if the resource exists for the user
     */
    public boolean verifySecurityResource(String resource) {
        return securityResources.contains(resource);
    }


    public void clearAllData() {
        securityResources.clear();
        username = null;
        logon = null;
    }

    @Override
    public CredentialsBean clone() {
        CredentialsBean d;
        try {
            d = this.getClass().newInstance();
        }
        catch (Exception ex) {
            throw new RuntimeException("stemcell..security.cloneAuthenticationData", ex);
        }
        d.username = username;
        d.logon = logon;
        d.securityResources = securityResources;
        return d;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return (username != null) ? username : "";
    }
}