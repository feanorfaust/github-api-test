package com.omyronets.githubapitest.domain;

import java.util.Base64;

public record SourceFile(String path, String content) {

    public SourceFile {
        content = content.replace("\n", "");
        content = new String(Base64.getDecoder().decode(content));
    }
}
