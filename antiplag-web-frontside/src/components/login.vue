<template>
    <div id="login">
        <div class="alert alert-success" role="alert">
            <h4 class="alert-heading text-center">在线查重系统</h4>
            <hr>
            <div class="form mt-3 mx-auto pt-3 w-75 px-2">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text d-inline-block">账 户</span>
                    </div>
                    <input v-model="loginInfo.username" type="text" class="form-control no-box-shadow" aria-label="" aria-describedby="basic">
                </div>
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text d-inline-block">密 码</span>
                    </div>
                    <input v-model="loginInfo.pwd" type="password" class="form-control no-box-shadow" aria-label="Text input with dropdown button">
                    <div class="input-group-append">
                        <button v-if="loginInfo.username===''||loginInfo.pwd===''" class="btn btn-outline-success no-box-shadow" type="button" disabled>登录</button>
                        <button v-else class="btn btn-outline-success no-box-shadow" @click="login" type="button">登录</button>
                    </div>
                </div>
                <div v-if="loginError" class="alert alert-warning alert-dismissible fade show small py-2" role="alert">
                    登录失败!
                    <button type="button" class="close btn-sm py-1 no-box-shadow" aria-label="Close" @click="loginError = false">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div>
            <hr>
            <div class="w-100 d-flex justify-content-end">
                <button type="button" data-toggle="modal" data-target="#registerModalCenter" class="btn btn-light btn-sm py-0 small no-box-shadow">还没有账号?</button>
            </div>
        </div>

        <div class="modal fade" id="registerModalCenter" tabindex="-1" role="dialog" aria-labelledby="registerModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="registerModalCenterTitle">没有账号?</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        本系统为测试系统，测试账号为:<br/>
                        <kbd>账户:</kbd> {{testAccount.username}}<br/>
                        <kbd>密码:</kbd> {{testAccount.pwd}}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-dark btn-sm no-box-shadow" data-dismiss="modal" @click="loginInfo = testAccount">填入账户和密码</button>
                    </div>
                </div>
            </div>
        </div>

        <github-corner user-name="mooyyu" repository-name="antiplag-web"/>
        <bottom-footer/>
    </div>
</template>

<script>
    import githubCorner from "@/components/githubCorner";
    import bottomFooter from "@/components/bottomFooter";

    export default {
        name: "login",
        components: {
            githubCorner, bottomFooter
        },
        data() {
            return {
                loginInfo: {
                    username: "",
                    pwd: ""
                },
                testAccount: {
                    username: "mooyyu",
                    pwd: "pwdfortest"
                },
                loginError: false
            }
        },
        methods: {
            login() {
                this.loginError = false;
                this.axios.get(this.host + '/login/login', {
                    params: {
                        name: this.loginInfo.username,
                        password: this.loginInfo.pwd
                    }
                }).then(res => {
                    if (res.data) this.$parent.isLogin = true;
                    else {
                        this.loginInfo = {
                            username: "",
                            pwd: ""
                        };
                        this.loginError = true;
                    }
                })
            }
        }
    }
</script>

<style lang="less" scoped>
    .no-box-shadow:focus {
        box-shadow: none!important;
        outline: none;
    }
    div#login {
        position: absolute;
        margin-top: -200px;
        margin-left: -300px;
        top: 50%;
        left: 50%;
        height: 360px;
        width: 600px;
    }
</style>