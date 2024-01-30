package com.omyronets.githubapitest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReviewComment(String body, @JsonProperty("commit_id") String commitId, String path, int line) {

}
