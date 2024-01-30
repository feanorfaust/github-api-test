package com.omyronets.githubapitest.web;

import com.omyronets.githubapitest.domain.PullRequestWebhook;
import com.omyronets.githubapitest.service.GitHubConsumer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final GitHubConsumer gitHubConsumer;

    public WebhookController(GitHubConsumer gitHubConsumer) {
        this.gitHubConsumer = gitHubConsumer;
    }


    //TODO need to configure IP filtering to avoid spoofed requests see https://docs.github.com/en/webhooks/using-webhooks/best-practices-for-using-webhooks#allow-githubs-ip-addresses
    //TODO need to reply with 200 within 10 sec see https://docs.github.com/en/webhooks/using-webhooks/best-practices-for-using-webhooks#respond-within-10-seconds
    @PostMapping
    public void printWebHook(@RequestBody PullRequestWebhook pullRequestWebhook) {
        if (pullRequestWebhook.action().equals("opened")) {
            gitHubConsumer.savePullRequestFilesChanged(pullRequestWebhook);

        }
    }

}
