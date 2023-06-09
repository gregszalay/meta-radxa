# DO NOT EDIT THIS FILE
#
# Please edit /boot/armbianEnv.txt to set supported parameters
#
setenv load_addr "0x39000000"
setenv overlay_error "false"
# default values
setenv verbosity "7"
setenv console "serial"
setenv rootfstype "ext4"
setenv docker_optimizations "on"
run mender_setup
echo "Boot script loaded from ${mender_uboot_boot}"
if test -e ${mender_uboot_root} /boot/armbianEnv.txt; then
        load ${mender_uboot_root} ${load_addr} /boot/armbianEnv.txt
        env import -t ${load_addr} ${filesize}
fi
if test "${logo}" = "disabled"; then setenv logo "logo.nologo"; fi
if test "${console}" = "display" || test "${console}" = "both"; then setenv consoleargs "console=tty1"; fi
if test "${console}" = "serial" || test "${console}" = "both"; then setenv consoleargs "${consoleargs} console=ttyS2,1500000n8"; fi
setenv verbosity "7"
# get PARTUUID of first partition on SD/eMMC the boot script was loaded from
if test "${devtype}" = "mmc"; then part uuid mmc ${devnum}:1 partuuid; fi
setenv bootargs "root=${mender_kernel_root} rootwait rootfstype=${rootfstype} ${consoleargs} panic=10 consoleblank=0 loglevel=${verbosity} ubootpart=${partuuid} usb-storage.quirks=${usbstoragequirks} ${extraargs} ${extrabootargs}"
if test "${docker_optimizations}" = "on"; then setenv bootargs "${bootargs} cgroup_enable=cpuset cgroup_memory=1 cgroup_enable=memory swapaccount=1"; fi
load ${mender_uboot_root} ${ramdisk_addr_r} /boot/uInitrd
load ${mender_uboot_root} ${kernel_addr_r} /boot/Image
load ${mender_uboot_root} ${fdt_addr_r} /boot/dtb/${fdtfile}
fdt addr ${fdt_addr_r}
fdt resize 65536
for overlay_file in ${overlays}; do
        if load ${mender_uboot_root} ${load_addr} /boot/dtb/rockchip/overlay/${overlay_prefix}-${overlay_file}.dtbo; then
                echo "Applying kernel provided DT overlay ${overlay_prefix}-${overlay_file}.dtbo"
                fdt apply ${load_addr} || setenv overlay_error "true"
        fi
done
for overlay_file in ${user_overlays}; do
        if load ${mender_uboot_root} ${load_addr} /boot/overlay-user/${overlay_file}.dtbo; then
                echo "Applying user provided DT overlay ${overlay_file}.dtbo"
                fdt apply ${load_addr} || setenv overlay_error "true"
        fi
done
if test "${overlay_error}" = "true"; then
        echo "Error applying DT overlays, restoring original DT"
        load ${mender_uboot_root} ${fdt_addr_r} /boot/dtb/${fdtfile}
else
        if load ${mender_uboot_root} ${load_addr} /boot/dtb/rockchip/overlay/${overlay_prefix}-fixup.scr; then
                echo "Applying kernel provided DT fixup script (${overlay_prefix}-fixup.scr)"
                source ${load_addr}
        fi
        if test -e ${devtype} ${devnum} /boot/fixup.scr; then
                load ${mender_uboot_root} ${load_addr} /boot/fixup.scr
                echo "Applying user provided fixup script (fixup.scr)"
                source ${load_addr}
        fi
fi
booti ${kernel_addr_r} ${ramdisk_addr_r} ${fdt_addr_r}
run mender_try_to_recover
# Recompile with:
# mkimage -C none -A arm -T script -d boot.cmd boot.scr