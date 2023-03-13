<#assign moduleName="${dataBase.camelName}" />
<#assign pathName="${table.tableLowerName}" />
<#assign pkAttrname="${table.camelKeyName[0]}" />

<template>
    <el-dialog :visible.sync="visible" :title="!dataForm.${pkAttrname} ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
            <#list table.columns as column>
                <#if !column.isKey && column.columnSqlName != "creator" && column.columnSqlName != "create_date">
                    <el-form-item label="${column.comment}" prop="${column.columnCamelName}">
                        <el-input v-model="dataForm.${column.columnCamelName}" placeholder="${column.comment}"></el-input>
                    </el-form-item>
                </#if>
            </#list>
        </el-form>
        <template slot="footer">
            <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
            <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
        </template>
    </el-dialog>
</template>

<script>
    import debounce from 'lodash/debounce'
    export default {
        data () {
            return {
                visible: false,
                dataForm: {
            <#list table.columns as column>
                    ${column.columnCamelName}:''<#if column_has_next>,</#if>
            </#list>
        }
        }
        },
        computed: {
            dataRule () {
                return {
                <#list table.columns as column>
                <#if !column.isKey && column.columnSqlName != "creator" && column.columnSqlName != "create_date">
                ${column.columnCamelName}:[
                    { required: true, message: this.$t('validate.required'), trigger: 'blur' }
                ]<#if column_has_next>,</#if>
                </#if>
                </#list>
            }
            }
        },
        methods: {
            init () {
                this.visible = true
                this.$nextTick(() => {
                    this.$refs['dataForm'].resetFields()
                    if (this.dataForm.${pkAttrname}) {
                        this.getInfo()
                    }
                })
            },
            // 获取信息
            getInfo () {
            this.$http.get(`/${moduleName}/${pathName}/<#noautoesc>$</#noautoesc>{this.dataForm.${pkAttrname}}`).then(({ data: res }) => {
                    if (res.code !== 0) {
                    return this.$message.error(res.msg)
                    }
                    this.dataForm = {
                        ...this.dataForm,
                        ...res.data
                    }
                }).catch(() => {})
            },
            // 表单提交
            dataFormSubmitHandle: debounce(function () {
            this.$refs['dataForm'].validate((valid) => {
                    if (!valid) {
                        return false
                    }
                this.$http[!this.dataForm.${pkAttrname} ? 'post' : 'put']('/${moduleName}/${pathName}/', this.dataForm).then(({ data: res }) => {
                        if (res.code !== 0) {
                        return this.$message.error(res.msg)
                        }
                        this.$message({
                    message: this.$t('prompt.success'),
                        type: 'success',
                            duration: 500,
                            onClose: () => {
                            this.visible = false
                            this.$emit('refreshDataList')
                        }
                    })
                    }).catch(() => {})
                })
            }, 1000, { 'leading': true, 'trailing': false })
        }
    }
</script>
