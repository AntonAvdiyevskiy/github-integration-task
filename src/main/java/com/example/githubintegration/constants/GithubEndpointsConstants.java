package com.example.githubintegration.constants;

public class GithubEndpointsConstants {

    public static final String GITHUB_DOMAIN = "https://api.github.com";

    public static final class ReposEndpoints {
        public static final String REPOS_ENDPOINT = GITHUB_DOMAIN + "/repos/" + "%s/" + "%s";
        public static final String BRANCHES_ENDPOINT = REPOS_ENDPOINT + "/branches?page={page}&per_page={per_page}";
    }

    public static final class UserEndpoints{
        public static final String USER_ENDPOINT = GITHUB_DOMAIN + "/users/" + "%s";

        public static final String USER_REPOS_ENDPOINT = USER_ENDPOINT + "/repos?page={page}&per_page={per_page}";
    }

    public static final class ErrorMessages{
        public static final String RESOURCE_NOT_FOUND_MSG = "Resource not found";

    }
}
