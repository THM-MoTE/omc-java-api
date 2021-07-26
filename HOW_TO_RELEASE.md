# How to release a new version of MoPE

This is a developer documentation for releasing new versions of this project on MavenCentral.

## Step 0: Make your changes and update the version number

The version number in `build.gradle` has to be changed, because once an artifact has been uploaded to a Maven repository, it cannot be changed.

## Step 1: Get an OSSRH account 

You need a JIRA account for the OSSRH (OSS Repository Hosting).
[Sign up](https://issues.sonatype.org/secure/Signup!default.jspa) directly, or read the [publishing guide](https://central.sonatype.org/publish/publish-guide/) for more information.

## Step 2: Get the access rights

Unless you're C. Schölzel, M. Hoppe, or N. Justus, you probably do not have the rights to do this.
I have absolutely no idea how you can get them, but you can probably still find [C. Schölzel](https://github.com/CSchoel/) on GitHub and he (meaning "I" at the time of writing) will be happy to help you out. 

## Step 3: Create and upload a GPG-key for signing the project artifacts

You need to sign the project artifacts with the [Gradle signing plugin](https://docs.gradle.org/current/userguide/signing_plugin.html) in order to be able to publish a Maven release.
In short, this means you have to use the following commands:

```bash
# generate the key
# use the default options
# the "comment" part in the ID is helpful so that you do not become like me
# and have multiple GPG keys lying around that you whish you knew what they
# were for ;P
gpg --full-generate-key
# export keys to a file that can be read by the gradle plugin
gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
# upload the GPG key so it can be verified by others
gpg --keyserver keys.openpgp.org --send-key keyId
# view your keys in order to extract the key ID (last 8 characters of the
# long garbled hash-like thing you are seeing for each key)
gpg -K
```

## Step 4: Add all relevant credentials to gradle.properties

Generate a `gradle.properties` file in the main repository folder with the following content:

```properties
ossrhUsername = YourUsername
ossrhPassword = YourPassword

signing.keyId=YourKeyID
signing.password=YourKeyPassword
signing.secretKeyRingFile=/home/usernameGoesHere/.gnupg/secring.gpg
```

I hope it is self-explanatory that you *do not commit* this file to the repository. ;)

## Step 5: Upload the new release

Use the gradle task `uploadArchives` to compile the project, sign it and upload it to [oss.sonatype.org](https://oss.sonatype.org/#stagingRepositories).
The task `uploadArchives` might only appear after you have refreshed your gradle config in your IDE, because the `build.gradle` contains a conditional statement that disables the task when no `ossrhUsername` is found.

When you [visit the Nexus Repository Manager](https://oss.sonatype.org/#stagingRepositories), you should now see a new "staging" repository.
There are three stages to such a "staging repository":

* `Open` means you have uploaded the artifacts, but they do not have been checked.
* `Closed` means the artifacts have passed all required checks and are ready for release.
* `Released` means you are done, and the new version is available for download.

So in order to proceed, we now have to "close" our upload - pretty counter-intuitive wording I think.
We can do this by using the gradle task `closeRepository`.
After you have done this, you should again check the website for any errors.
If there are none, you can proceed with the final task `promoteRepository`.
If not, good luck fixing the errors - I would be pleased if you extend this document with any additional steps and information required.

Note: It may take about 10 minutes until the new version is available from maven central.