package com.omyronets.githubapitest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileChanged(@JsonProperty("filename") String fileName, @JsonProperty("contents_url") String contentsUrl, String patch) {

}
