package com.omyronets.githubapitest.service;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.omyronets.githubapitest.domain.FileChanged;
import com.omyronets.githubapitest.domain.PullRequest;
import com.omyronets.githubapitest.domain.PullRequestWebhook;
import com.omyronets.githubapitest.domain.ReviewComment;
import com.omyronets.githubapitest.domain.SourceFile;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GitHubConsumer {

    private final GitHubAuthService gitHubAuthService;

    @Value("${github.api.url}")
    private String baseURL;

    @Value("${github.api.version}")
    private String apiVersion;

    public GitHubConsumer(GitHubAuthService gitHubAuthService) {
        this.gitHubAuthService = gitHubAuthService;
    }

    public void savePullRequestFilesChanged(PullRequestWebhook pullRequestWebhook) {
        RestClient restClient = RestClient.create();
        String installationId = pullRequestWebhook.installation().id();
        String pullRequestUrl = pullRequestWebhook.pullRequest().url();
        String authToken = gitHubAuthService.generateAppInstallationToken(installationId);
        List<FileChanged> files = restClient.get()
                                            .uri(pullRequestUrl + "/files")
                                            .accept(MediaType.ALL)
                                            .header("X-GitHub-Api-Version", apiVersion)
                                            .header("Authorization", "Bearer " + authToken)
                                            .retrieve()
                                            .body(new ParameterizedTypeReference<>() {
                                            });
        pullRequestWebhook.pullRequest().files().addAll(files);
        printFilesContentsWithPatch(files, installationId);
        for (FileChanged file : files) {
            String text = "СЮЮЮЮЮДААА!!!!";
            String sha =  pullRequestWebhook.pullRequest().head().sha();
            String path = file.fileName();
            int line = 1;
            ReviewComment comment = new ReviewComment(text,
                                                      sha,
                                                      path,
                                                      line);
            addReviewComment(pullRequestWebhook.pullRequest(), comment,installationId);
        }

    }

    public void printFilesContentsWithPatch(List<FileChanged> files, String installationId) {
        RestClient restClient = RestClient.create();

        String authToken = gitHubAuthService.generateAppInstallationToken(installationId);
        for (FileChanged file : files) {
            SourceFile sourceFile = restClient.get()
                                              .uri(URI.create(file.contentsUrl()))
                                              .accept(MediaType.ALL)
                                              .header("X-GitHub-Api-Version", apiVersion)
                                              .header("Authorization", "Bearer " + authToken)
                                              .retrieve()
                                              .body(SourceFile.class);

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Filename: " + file.fileName());
            System.out.println("File contents:");
            System.out.println(sourceFile.content());
            System.out.println("File diff:");
            System.out.println(file.patch());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println();
        }

    }

    public void addReviewComment(PullRequest pullRequest, ReviewComment comment, String installationId) {
        String authToken = gitHubAuthService.generateAppInstallationToken(installationId);
        RestClient restClient = RestClient.create();
        restClient.post()
                  .uri(pullRequest.url() + "/comments")
                  .contentType(APPLICATION_JSON)
                  .accept(MediaType.ALL)
                  .header("Authorization", "Bearer " + authToken)
                  .body(comment)
                  .retrieve()
                  .toBodilessEntity();
    }

}
