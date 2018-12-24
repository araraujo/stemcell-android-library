package br.com.stemcell.android.beans;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Value Object de transporte de dados de segurança do usuário
 * @author x4ij
 */
public class CredentialsBean implements Serializable {
    /**
     * Armazena a chave de um usuário autenticado
     */
    private String logon;
    /**
     * Armazena o nome de um usuário autenticado
     */
    private String username;
    /**
     * Armazenas os recursos do controle de acesso permitidos ao usuário, a fim
     * de transporte entre servidor e cliente remoto
     */
    private Set<String> securityResources;

    /**
     * Construtor
     */
    public CredentialsBean() {
    }

    /**
     * Construtor
     * @param logon chave do usuario
     * @param username Nome do usuario
     * @param securityResources recursos de segurança
     */
    public CredentialsBean(String logon, String username, Set securityResources) {
        this.logon = logon;
        this.username = username;
        this.securityResources = new HashSet<String>(securityResources);
    }

    /**
     * @return Logon do usuário (chave)
     */
    public String getLogon() {
        return logon;
    }

    public void setLogon(String logon) {
        this.logon = logon;
    }

    /**
     * @return Nome do usuário logado
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return true se está autenticado
     */
    public boolean isAuthenticated() {
        return username != null;
    }

    /**
     * @return Lista de recursos de segurança do usuário
     */
    public Set<String> getSecurityResources() {
        return Collections.unmodifiableSet(securityResources);
    }

    public void setSecurityResources(Set<String> securityResources) {
        this.securityResources = securityResources;
    }

    /**
     * Verifica se algum recurso existe para o usuário
     * @param asList lista de recursos
     * @return true se algum recurso existe para o usuário
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
     * Verifica o recurso existe para o usuário
     * @param resource recurso
     * @return true se o recurso existe para o usuário
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
            throw new RuntimeException("snarf.security.cloneAuthenticationData", ex);
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