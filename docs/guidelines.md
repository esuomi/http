# Project versioning

The project lifecycle versioning follows the [Semantic Versioning](http://semver.org/) guidelines. When creating Pull 
Requests to an in-development version branch, be aware of the level of changes you commit. This versioning scheme relies
on build automation in conjunction with [Continuous Integration](http://en.wikipedia.org/wiki/Continuous_integration).

## Branching model

Repository should have one mainline branch, usually named `master`, to which everything is eventually merged to. Other rules may be applied on top of this based on project specific needs.

## Tagging versions

All releases are marked to the repository with [annotated tags](http://git-scm.com/book/en/v2/Git-Basics-Tagging#Annotated-Tags). Tagging should be handled by build automation. Tag names should follow the pattern `v{MAJOR}.{MINOR}.{PATCH}`. The name of the tag should follow Semantic Versioning's rules exactly, including identifiers if necessary. All tags should be considered eternal. Branches, tags and other Git metadata should never be removed from the main repository. 

> **Example:**
>  Third release candidate for version 2.3.1 of the project would be tagged as `v2.3.1-rc.3` in the commit present in `master` branch.

If multiple versions are to be maintained, branch should be forked from `master` at the latest compatible tag commit with name matching pattern `v{MAJOR}.{MINOR}.x` and all further patch level version tags alongside with the commits should appear into this branch.

## Working with versions

Usage of snapshots should be avoided whenever possible. This is not a hard set rule and sometimes unavoidable, especially in the early stages of project's lifecycle where no actual releases have occurred yet. If project requires

The releases may be preceded by commonly used pre-release identifiers, namely Alpha, Beta and RC. Whether these are used is up to the project itself. GA for final releases may be optionally used.