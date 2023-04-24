DESCRIPTION = "Amlogic Firmware In Package - Prebuilt binaries/tools to pack Amlogic bootloader"

LICENSE = "BINARY"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2dbd68496cc5ed3e68e855100cb86363"
NO_GENERIC_LICENSE[BINARY] = "LICENSE"

SRC_URI = "git://github.com/radxa/fip.git;branch=master;protocol=https;"
SRCREV = "5b8beba2f50ce06219c4e4844101226a81aef854"

inherit native

S = "${WORKDIR}/git"

do_install() {
	mkdir -p ${D}/${datadir}/radxa-amlogic-fip
	cp -r ${S}/* ${D}/${datadir}/radxa-amlogic-fip
}
