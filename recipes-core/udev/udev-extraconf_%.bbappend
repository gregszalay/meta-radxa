# extra configuration udev rules
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
	file://10-rk.rules \
"

do_install:prepend () {
	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/*.rules ${D}${sysconfdir}/udev/rules.d
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
