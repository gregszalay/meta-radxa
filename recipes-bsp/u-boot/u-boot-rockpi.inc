require u-boot.inc

HOMEPAGE = "https://github.com/radxa/u-boot"
LICENSE = "GPLv2+"
COMPATIBLE_MACHINE = "(px30|rk3036|rk3066|rk3188|rk3288|rk3328|rk3399|rk3308|rk3399pro|rk3566|s905y2)"
SECTION = "bootloaders"
DEPENDS = "dtc-native bc-native swig-native bison-native coreutils-native"

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
	# copy to default search path
	if [ ${SPL_BINARY} ]; then
		cp ${B}/spl/${SPL_BINARY} ${B}/
	fi
	if [ -f "${WORKDIR}/${MACHINE}/boot.cmd" ]; then
		cp ${WORKDIR}/${MACHINE}/boot.cmd ${WORKDIR}/boot.cmd
		${B}/tools/mkimage -C none -A arm -T script -d ${WORKDIR}/boot.cmd ${WORKDIR}/boot.scr
	fi
}

do_install:append() {
	if [ -f "${WORKDIR}/boot.scr" ]; then
		install -m 644 ${WORKDIR}/boot.scr ${D}/boot/
	fi
	if [ -f "${WORKDIR}/${MACHINE}/uEnv.txt" ]; then
		install -m 644 ${WORKDIR}/${MACHINE}/uEnv.txt ${D}/boot/
	fi
}

do_deploy:append() {
	cp -a ${B}/tools/mkimage ${DEPLOY_DIR_IMAGE}
	if [ -f "${WORKDIR}/${MACHINE}/uEnv.txt" ]; then
		install -D -m 644 ${WORKDIR}/${MACHINE}/uEnv.txt ${DEPLOYDIR}/
	fi
	if [ -f "${WORKDIR}/boot.scr" ]; then
		install -D -m 644 ${WORKDIR}/boot.scr ${DEPLOYDIR}/
	fi
	if [ -f "${WORKDIR}/boot.cmd" ]; then
		install -D -m 644 ${WORKDIR}/boot.cmd ${DEPLOYDIR}/
	fi
}

PACKAGE_BEFORE_PN = "${PN}-scripts"
FILES_${PN}-scripts = " \
	/boot/boot.scr \
	/boot/uEnv.txt \
"
