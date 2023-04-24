DEFAULT_PREFERENCE = "1"

DESCRIPTION = "Radxa Zero U-Boot"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

include u-boot-rockpi.inc

inherit python3native

DEPENDS += "radxa-amlogic-fip-native"

SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=radxa-zero-v2021.07;protocol=https; \
	file://${MACHINE}/boot.cmd \
	file://${MACHINE}/uEnv.txt \
"

SRCREV = "5d5248841446eb4826ef16c71e2750a81b05a22d"

do_compile:append() {
	cp ${B}/u-boot.bin ${STAGING_DATADIR_NATIVE}/radxa-amlogic-fip/radxa-zero/bl33.bin
	cd ${STAGING_DATADIR_NATIVE}/radxa-amlogic-fip/radxa-zero && make
}

do_install:append() {
	install -m 644 ${STAGING_DATADIR_NATIVE}/radxa-amlogic-fip/radxa-zero/u-boot* ${D}/boot/
}

do_deploy:append() {
	install -D -m 644 ${STAGING_DATADIR_NATIVE}/radxa-amlogic-fip/radxa-zero/u-boot.bin ${DEPLOYDIR}/
}
