<template>
    <div id="app">
        <main-panel v-if="isLogin"/>
        <login v-else/>
    </div>
</template>

<script>
    import mainPanel from "@/components/mainPanel";
    import login from "@/components/login";

    export default {
        name: 'app',
        components: {
            mainPanel, login
        },
        data() {
            return {
                isLogin: false
            }
        },
        mounted() {
            this.isLogin = false;
            this.axios.get(this.host + '/login/isLogin').then(res => {
                if (res.data) this.isLogin = true;
            })
        }
    }
</script>

<style lang="less">
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
        user-select: none;
    }
    .no-box-shadow:focus {
        box-shadow: none!important;
    }
</style>
