DESCRIPTION = "Linux kernel for RockPi-4"

inherit kernel
inherit python3native
require recipes-kernel/linux/linux-yocto.inc

DEPENDS += "openssl-native"

SRC_URI = " \
	git://github.com/radxa/kernel.git;branch=release-4.4-rockpi4;protocol=https; \
	file://0002-Suppress-additional-warnings.patch \
	file://brcmfmac.scc \
"

SRCREV = "86a614bc15b3b1aeb3a9a9e395aedd088c70e35e"
LINUX_VERSION = "4.4.154"
KCONFIG_MODE = "alldefconfig"

# Override local version in order to use the one generated by linux build system
# And not "yocto-standard"
LINUX_VERSION_EXTENSION = ""
PR = "r1"
PV = "${LINUX_VERSION}"

# Include only supported boards for now
COMPATIBLE_MACHINE = "(rk3036|rk3066|rk3288|rk3328|rk3399|rk3308)"

do_compil:append() {
	oe_runmake dtbs
}

# Make sure we use /usr/bin/env ${PYTHON_PN} for scripts
do_patch:append() {
	for s in `grep -rIl python ${S}/scripts`; do
		sed -i -e '1s|^#!.*python[23]*|#!/usr/bin/env ${PYTHON_PN}|' $s
	done
}

do_deploy:append() {
	install -d ${DEPLOYDIR}/overlays
	install -m 644 ${WORKDIR}/linux-rockpi_4*/arch/arm64/boot/dts/rockchip/overlays-rockpi4/* ${DEPLOYDIR}/overlays
	install -m 644 ${S}/arch/arm64/boot/dts/rockchip/overlays-rockpi4/hw_intfc.conf ${DEPLOYDIR}/
}
