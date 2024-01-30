package com.omyronets.githubapitest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PullRequestWebhook(String action, @JsonProperty("pull_request") PullRequest pullRequest,
                                 Installation installation) {

    public record Installation(String id) {

    }
}
