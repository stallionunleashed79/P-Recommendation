#!/usr/bin/env bash

display_header() {
echo
echo "=============================================================================="
echo " $1"
echo "=============================================================================="
echo
}

succ_msg(){
printf '\033[33mSUCCESS: %s \033[0m\n\n' "$*";
}

fail_msg(){
printf '\033[31mFAIL: %s \033[0m\n\n' "$*";
}

info_msg(){
printf '\033[37m%s \033[0m\n' "$*";
}

warn_msg(){
printf '\033[33mWARNING: %s \033[0m\n' "$*";
}

print_msg(){
printf  '%s \n' "$*";
}