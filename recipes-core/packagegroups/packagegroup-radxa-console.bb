SUMMARY = "Organize packages to avoid duplication across all images"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# contains basic dependencies, that can work without graphics/display
RDEPENDS:packagegroup-radxa-console = "\
    coreutils \
    cpufrequtils \
    gnupg \
    hostapd \
    htop \
    iptables \
    iproute2 \
    kernel-modules \
    networkmanager \
    networkmanager-nmtui \
    openssh-sftp-server \
    dialog \
    i2c-tools \
    net-tools \
    findutils \
    mraa-radxa-dev \
    mraa-radxa-utils \
    util-linux \
    nano \
    strace \
    resize-helper \
    ntp \
    kmod \
    gptfdisk \
    screen \
    "
