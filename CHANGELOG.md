# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

No changes yet

## [0.3.6] - 2021-07-26

### Changed

* `OMVersion` now supports any version that *contains* a semantic version string which specifies at least the major version.
    This also means that it does *not* work if a number occurs in the version string *before* the actual version number.
    This change was required, because the scheme for the version string changed in newer OpenModelica versions and the old system proved to be too rigid.

### Fixed

* The tests for `killTrailingQuotes` assumed that the result was trimmed *after* quotes were removed, but it was actually trimmed *before*.
    I do not know what is the intended behavior of the method, but I chose to leave it as it is and change the tests instead, so that the change does not break any working code.
* `SemanticVersion` no longer accepts empty strings.

## [0.3.5] - 2018-12-05

This version drops the CORBA interface in favour of ZeroMQ.
CORBA will be removed from java in java-11 and onwards, therefore the switch to ZeroMQ.

This release requires at least OpenModelica Version 1.12.0. Before 1.12.0, there is no ZeroMQ support and it will not work.

[Unreleased]: https://github.com/THM-MoTE/omc-java-api/compare/v0.3.6...HEAD
[0.3.6]: https://github.com/THM-MoTE/omc-java-api/compare/V-0.3.5...v0.3.6
[0.3.5]: https://github.com/THM-MoTE/omc-java-api/releases/tag/V-0.3.5
