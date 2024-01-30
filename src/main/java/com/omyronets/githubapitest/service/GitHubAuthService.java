package com.omyronets.githubapitest.service;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.omyronets.githubapitest.domain.InstallationToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.io.FileReader;
import java.io.Reader;
import java.security.PrivateKey;
import java.util.Date;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestClient;

@Service
public class GitHubAuthService {

    private final String CERTIFICATE_PATH = "keys/";

    @Value("${github.authentication.keystoreCertificate}")
    private String keystoreCertificate;

    @Value("${github.authentication.tokenExpirationPeriod}")
    private int expirationPeriod;

    @Value("${github.app.id}")
    private String appId;

    @Value("${github.api.url}")
    private String baseURL;

    @Value("${github.api.version}")
    private String apiVersion;

    public String generateGitHubAppJwtToken() {
        PrivateKey privateKey = getPrivateKey(CERTIFICATE_PATH + keystoreCertificate);
        return Jwts.builder()
                   .issuer(appId)
                   .issuedAt(new Date())
                   .expiration(new Date((new Date()).getTime() + expirationPeriod))
                   .signWith(privateKey, SIG.RS256)
                   .compact();
    }

    public String generateAppInstallationToken(String installationID) {
        RestClient restClient = RestClient.create();
        return restClient.post()
                         .uri(baseURL + "/app/installations/" + installationID + "/access_tokens")
                         .accept(APPLICATION_JSON)
                         .header("X-GitHub-Api-Version", apiVersion)
                         .header("Authorization", "Bearer " +generateGitHubAppJwtToken())
                         .retrieve()
                         .body(InstallationToken.class)
                         .token();
    }

    private static PrivateKey getPrivateKey(String filename) {
        try (Reader reader = new FileReader(ResourceUtils.getFile("classpath:" + filename)); PEMParser pemParser = new PEMParser(reader)) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PEMKeyPair object = (PEMKeyPair) pemParser.readObject();
            return converter.getPrivateKey(object.getPrivateKeyInfo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
