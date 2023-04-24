

DEFAULT_PREFERENCE = "1"

DESCRIPTION = "Radxa CM3 IO U-Boot"
DEPENDS += "u-boot-mkimage-radxa-native"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

#disable automatic patching by mender
MENDER_UBOOT_AUTO_CONFIGURE = "0"
BOOTENV_SIZE = "0x8000"

#do_compile[depends] += "radxa-binary-loader:do_deploy"
do_compile[depends] += " \
        radxa-binary-loader:do_populate_lic \
"
do_compile[depends] += " \
        radxa-binary-native:do_populate_sysroot \
        radxa-binary-loader:do_deploy \
"

include u-boot-rockpi.inc

SRC_URI = " \
	git://github.com/gregszalay/u-boot.git;branch=stable-4.19-rock3-mender;protocol=https; \
	file://0001-Use-local-command.h-file-instead-of-system-file.patch \
	file://0002-Suppress-maybe-uninitialized-warning.patch \
	file://0003_Fix-failed_to_create_atf.patch \
	file://${MACHINE}/0001-to-avoid-warnings-when-compiling-with-GCC-8.1.patch \
	file://${MACHINE}/0002-fs-ext4-cache-extent-data.patch \
	file://${MACHINE}/0003-fs-ext4-Fix-alignment-of-cache-buffers.patch \
	file://${MACHINE}/0004-mkimage-Fix-header-generation-of-boot.scr.patch \
	file://${MACHINE}/boot.cmd \
	file://${MACHINE}/uEnv.txt \
"
##
#	file://0001-Add-missing-header-which-fails-on-recent-GCC.patch \
#	file://0002-Generic-boot-code-for-Mender.patch \
#	file://0003-Integration-of-Mender-boot-code-into-U-Boot.patch \
#	file://0004-add-config_mender_defines.h.patch \
#	file://0005-configs-rockpro64-add-Mender-support.patch \
#	file://0006-RockPro64-eMMC-integration-for-Mender.patch \
#	file://0007-radxa-cm3-integration-for-Mender.patch \	
#"

#EXTRA_OEMAKE = "'HOSTCC=${BUILD_CC}'"

# gergely 
#SRCREV = "431859b8e2cfa5a7f243edfa5738d4e2b11f4d27"
SRCREV = "${AUTOREV}"
#SRCREV = "693c4cd017e57a6af8e471494be5e8780c041b08"

do_compile:append () {
	oe_runmake -C ${S} O=${B}/${config} BL31=${DEPLOY_DIR_IMAGE}/radxa-binary/bl31.elf spl/u-boot-spl.bin u-boot.dtb u-boot.itb
	./tools/mkimage -n rk3568 -T rksd -d ${DEPLOY_DIR_IMAGE}/radxa-binary/ddr.bin:spl/u-boot-spl.bin ${B}/idbloader.img
}

do_deploy:append() {
	install -D -m 644 ${B}/u-boot.itb ${DEPLOYDIR}/
	install -m 644 ${B}/idbloader.img ${DEPLOY_DIR}/idbloader.img
}

do_install:append() {
	rm ${D}/boot -r
}
