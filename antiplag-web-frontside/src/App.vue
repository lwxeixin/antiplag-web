<template>
    <div id="app" class="mx-auto pt-5 rounded">
        <div class="main">
            <div class="alert alert-success text-center" role="alert">
                <h4 class="alert-heading">代码、文件查重系统</h4>
                <p>antiplag (web version)</p>
                <hr>
            </div>
            <div class="form p-3">
                <small class="form-text text-muted">
                    请上传一个不加密的zip压缩文件，待比较的文件应在压缩文件的根目录位置。
                </small>
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text">上传压缩文件</span>
                    </div>
                    <div class="custom-file">
                        <input type="file" accept="application/zip" class="custom-file-input" id="inputGroupFile01">
                        <label class="custom-file-label" for="inputGroupFile01"/>
                    </div>
                </div>
                <div class="form-radio">
                    <span>待比较文件类型: </span>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1" checked>
                        <label class="form-check-label" for="inlineRadio1">代码文件</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2">
                        <label class="form-check-label" for="inlineRadio2">文本文件</label>
                    </div>
                </div>
                <div>
                    <label class="mt-2 mb-0" for="range">设置相似度阈值: <span class="text-danger font-weight-bold font-italic">{{simValue}}</span></label>
                    <input v-model="simValue" type="range" class="custom-range" min="0" max="100" step="1" id="range">
                </div>
                <div class="row">
                    <div class="input-group mb-3 col-6">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupSelect01">检测工具</label>
                        </div>
                        <select class="custom-select" id="inputGroupSelect01">
                            <option value="1" selected>MOSS</option>
                            <option value="2">SIM</option>
                            <option value="3">jplag</option>
                        </select>
                    </div>
                    <div class="input-group mb-3 col-6">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupSelect02">程序语言</label>
                        </div>
                        <select class="custom-select" id="inputGroupSelect02">
                            <option value="1" selected>One</option>
                            <option value="2">Two</option>
                            <option value="3">Three</option>
                        </select>
                    </div>
                </div>
                <div class="submit w-100">
                    <button v-if="!submitting" @click="submit" type="button" class="btn btn-outline-success btn-sm mx-auto d-block">执行比较</button>
                    <button v-else type="button" class="btn btn-outline-success btn-sm mx-auto d-block" disabled>请等待片刻。。。</button>
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
        name: 'app',
        components: {
            githubCorner, bottomFooter
        },
        data() {
            return {
                submitting: false,
                simValue: 30
            }
        },
        mounted() {

        },
        methods: {
            submit() {
                this.axios.get(this.host + '/hello/getSessionId').then(res => {
                    // eslint-disable-next-line no-console
                    console.info(res.data);
                })
            }
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
    body {
        width: 100vw;
        height: 100vh;
    }
    div#app {
        width: 60vw;
        min-height: 300px;
        div.main {
            background-color: #c3e6cb;
        }
    }
</style>
