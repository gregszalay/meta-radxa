require u-boot.inc

DEFAULT_PREFERENCE = "1"

DESCRIPTION = "Rock-3A-RK3568 U-Boot"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"


SRC_URI = " \
	git://github.com/radxa/u-boot.git;branch=stable-4.19-rock3;protocol=https; \
	file://0003_Fix-failed_to_create_atf.patch \
"

SRCREV = "${AUTOREV}"

HOMEPAGE = "https://github.com/radxa/u-boot"
LICENSE = "GPLv2+"
COMPATIBLE_MACHINE = "(rk3568)"
SECTION = "bootloaders"
DEPENDS = "dtc-native bc-native swig-native bison-native coreutils-native python3-native"

S = "${WORKDIR}/git"

# u-boot will build native python module
inherit python3native

do_configure () {
    if [ -z "${UBOOT_CONFIG}" ]; then
        if [ -n "${UBOOT_MACHINE}" ]; then
            oe_runmake -C ${S} O=${B} ${UBOOT_MACHINE}
        else
            oe_runmake -C ${S} O=${B} oldconfig
        fi

        ${S}/scripts/kconfig/merge_config.sh -m .config ${@" ".join(find_cfgs(d))}
        cml1_do_configure
    fi
}

do_compile:prepend () {
	export STAGING_INCDIR=${STAGING_INCDIR_NATIVE};
	export STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE};
}

do_compile:append () {
	oe_runmake -C ${S} O=${B}/${config} BL31=${DEPLOY_DIR_IMAGE}/radxa-binary/bl31.elf spl/u-boot-spl.bin u-boot.dtb u-boot.itb
	./tools/mkimage -n rk3568 -T rksd -d ${DEPLOY_DIR_IMAGE}/radxa-binary/ddr.bin:spl/u-boot-spl.bin ${DEPLOY_DIR_IMAGE}/idbloader.img
}

do_deploy:append() {
	install -D -m 644 ${B}/u-boot.itb ${DEPLOYDIR}/
}

