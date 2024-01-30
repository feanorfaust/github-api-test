package com.omyronets.githubapitest.domain;

import java.util.ArrayList;
import java.util.List;

public record PullRequest(String url, Head head, List<FileChanged> files) {

    public PullRequest {
            files = new ArrayList<>();
    }

    public record Head(String sha) {

    }
}
