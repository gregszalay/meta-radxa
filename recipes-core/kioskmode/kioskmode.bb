SUMMARY = "autostart chromium on x-server in kiosk mode"

LICENSE = "CLOSED"

RDEPENDS_${PN} = "mini-x-session"

SRC_URI:append = " file://session"
FILE_${PN} += " ${sysconfdir}/mini_x/session"

do_install() {
	install -d ${D}${sysconfdir}/mini_x
	install -m 755 ${WORKDIR}/session ${D}${sysconfdir}/mini_x/session

	mkdir -p ${D}${sysconfdir}/systemd/system/multi-user.target.wants
	ln -sf /lib/systemd/system/xserver-nodm.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/xserver-nodm.service
}
