#
# $Header: /src/common/usc/skel/RCS/dot.login,v 1.4 1994/06/20 20:48:53 ucs Exp $
#

#
# See if the first time login script exists and run it if it does.
#
if (-r ~/.login.first) then
	source ~/.login.first
endif

###############################################################################
#
# Source a global .login file.
#
# DO NOT DELETE THIS SECTION!
#
# If you wish to customize your own environment, then add things AFTER
# this section.  In this way, you may override certain default settings,
# but still receive the system defaults.
#
if (-r /usr/lsd/conf/master.login) then
	source /usr/lsd/conf/master.login
else if (-r /usr/local/lib/master.login) then
	source /usr/local/lib/master.login
endif
###########################################################################
#
# Set the terminal type
#
if (-r /usr/lsd/conf/setterm.csh) then
	source /usr/lsd/conf/setterm.csh
else
	echo "WARNING: The set terminal type script (/usr/lsd/conf/setterm.csh)"
	echo "is not available on this system.  Thus, your terminal type"
	echo "is probably not set correctly."
endif
###########################################################################
