SOC_FAMILY = "rk3568"

require conf/machine/include/arm/arch-armv8a.inc
require conf/machine/include/soc-family.inc

MACHINEOVERRIDES =. "mali-gpu:mali-midgard:"

PREFERRED_PROVIDER_virtual/kernel = "linux-rock-3a"
PREFERRED_VERSION_linux-rockchip = "4.19"

SERIAL_CONSOLES = "1500000;ttyFIQ0"
KERNEL_IMAGETYPE = "Image"
KBUILD_DEFCONFIG = "rockchip_linux_defconfig"

PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-rock-3a"

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += "\
   kernel-modules \
   resize-helper \
"

IMAGE_FSTYPES += "rockchip-radxa-gpt-img"
IMAGE_CLASSES += "rockchip-radxa-gpt-img"
