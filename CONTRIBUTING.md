# Working with the repository

All contributions should be provided as Pull Requests following the **Fork & Pull** model described in GitHub's 
[Collaborating/Using pull requests](https://help.github.com/articles/using-pull-requests/) documentation.

## Development practices
 
 - **Branch name for contribution** should be descriptive of the full changeset, in all lowercase and individual words
   separated with dashes. For example `totp-authentication-support` is a good name while `scannable_QR_codes` is not.
 - At least the **first commit message in Pull Request** should follow the logic described in [Commit messages are not titles (antirez)](http://antirez.com/news/90)
 - **Do not use emojis**, GitHub specific icons or such in commit messages which may not render properly when viewed in some other system than GitHub.
 - When resolving a GitHub issue, **edit the Pull Request title and/or message to contain the trigger keyword** (*see [Closing issues via commit messages](https://help.github.com/articles/closing-issues-via-commit-messages/)*). Do not include the issue reference directly to the actual commit message.

## Legal gubbins

By submitting a patch you indicate that you are satisfied and agree to the license terms of the project. For the busy  people, see [tl;dr Legal page for MIT](https://tldrlegal.com/license/mit-license)

It is your responsibility to verify that the additional dependencies you submit are MIT license and open source compatible. This responsibility will not transition from you to the main project upon acceptance of your contribution.

## Reviewing Pull Requests

Allow the build automation (*if any*) to verify the Pull Request before merging. Other review policies should be agreed by the people working on the project, but it is generally advisable to have at least some kind of peer review process whenever possible, even in two person teams.