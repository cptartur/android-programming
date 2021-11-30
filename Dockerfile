FROM ubuntu:20.04

# update ubuntu
RUN apt update && apt upgrade -y

# install curl
RUN apt install -y zip unzip curl 

# add user
RUN useradd -ms /bin/bash artur
RUN adduser artur sudo

# install sdkman, jdk, gradle, kotlin
USER artur
WORKDIR /home/artur/
RUN curl -s "https://get.sdkman.io" | bash
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 11.0.13.8.1-amzn && sdk install gradle 7.2 && sdk install kotlin 1.5.31"

# add jdk, gradle, kotlin to PATH
ENV PATH=/home/artur/.sdkman/candidates/java/current/bin:$PATH
ENV PATH=/home/artur/.sdkman/candidates/gradle/current/bin:$PATH
ENV PATH=/home/artur/.sdkman/candidates/kotlin/current/bin:$PATH
