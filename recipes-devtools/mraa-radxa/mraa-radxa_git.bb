SUMMARY = "Linux Library for low speed I/O Communication"
HOMEPAGE = "https://github.com/radxa/mraa"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=91e7de50a8d3cf01057f318d72460acd"

SRCREV = "fc8c9061a6ee73e9910749a51a12424f7cb21c08"
PV = "2.1.0+git${SRCPV}"

SRC_URI = "git://github.com/radxa/mraa.git;protocol=https;branch=master; \
           file://0004-fix-version-already-defined.patch \
	   file://0005-mraa-extended-radxa-cm3-functionality.patch \
           "

S = "${WORKDIR}/git"

# CMakeLists.txt checks the architecture, only x86 and ARM supported for now
COMPATIBLE_HOST = "(x86_64.*|i.86.*|aarch64.*|arm.*)-linux"

inherit cmake setuptools3-base

DEPENDS += "json-c"

EXTRA_OECMAKE:append = " -DINSTALLTOOLS:BOOL=ON -DFIRMATA=ON -DCMAKE_SKIP_RPATH=ON \
                         -DPYTHON3_PACKAGES_PATH:PATH=${baselib}/python${PYTHON_BASEVERSION}/site-packages \
			 -DBUILDSWIGNODE=OFF \
                       "

# Prepend mraa-utils to make sure bindir ends up in there
PACKAGES =+ "${PN}-utils"

FILES:${PN}-doc += "${datadir}/mraa/examples/"

FILES:${PN}-utils = "${bindir}/"

PACKAGECONFIG ??= "python"

PACKAGECONFIG[python] = "-DBUILDSWIGPYTHON=ON, -DBUILDSWIGPYTHON=OFF, swig-native ${PYTHON_PN},"
PACKAGECONFIG[ft4222] = "-DUSBPLAT=ON -DFTDI4222=ON, -DUSBPLAT=OFF -DFTDI4222=OFF,, libft4222"

FILES_${PYTHON_PN}-${PN} = "${PYTHON_SITEPACKAGES_DIR}/"
RDEPENDS_${PYTHON_PN}-${PN} += "${PYTHON_PN}"
