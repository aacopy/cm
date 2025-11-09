<template>
  <ElDialog
      v-model="visible"
      :title="dialogType === 'add' ? '新增鉴权组' : '编辑鉴权组'"
      width="30%"
      align-center
      @close="handleClose"
  >
    <ElForm ref="formRef" :model="form" :rules="rules" label-width="120px">
      <ElFormItem label="名称" prop="name">
        <ElInput v-model="form.name" placeholder="请输入名称" />
      </ElFormItem>
      <ElFormItem label="编码" prop="code">
        <ElInput v-model="form.code" placeholder="请输入编码" />
      </ElFormItem>
      <ElFormItem label="描述" prop="description">
        <ElInput
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
        />
      </ElFormItem>
    </ElForm>
    <template #footer>
      <div class="dialog-footer">
        <ElButton @click="handleClose">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">提交</ElButton>
      </div>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import {saveAuthGroup} from '@/api/auth-group'

type AuthGroupListItem = Api.SystemManage.AuthGroupListItem

interface Props {
  modelValue: boolean
  dialogType: 'add' | 'edit'
  authGroupData?: AuthGroupListItem
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: false,
  dialogType: 'add',
  authGroupData: undefined
})

const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()

/**
 * 弹窗显示状态双向绑定
 */
const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

/**
 * 表单验证规则
 */
const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 1 到 5 个字符', trigger: 'blur' }
  ],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
})

/**
 * 表单数据
 */
const form = reactive<AuthGroupListItem>({
  id: 0,
  name: '',
  code: '',
  description: '',
  createTime: ''
})

/**
 * 监听弹窗打开，初始化表单数据
 */
watch(
    () => props.modelValue,
    (newVal) => {
      if (newVal) initForm()
    }
)

/**
 * 监听角色数据变化，更新表单
 */
watch(
    () => props.roleData,
    (newData) => {
      if (newData && props.modelValue) initForm()
    },
    { deep: true }
)

/**
 * 初始化表单数据
 * 根据弹窗类型填充表单或重置表单
 */
const initForm = () => {
  if (props.dialogType === 'edit' && props.roleData) {
    Object.assign(form, props.roleData)
  } else {
    Object.assign(form, {
      id: 0,
      name: '',
      code: '',
      description: '',
      createTime: ''
    })
  }
}

/**
 * 关闭弹窗并重置表单
 */
const handleClose = () => {
  visible.value = false
  formRef.value?.resetFields()
}

/**
 * 提交表单
 * 验证通过后调用接口保存数据
 */
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    await saveAuthGroup(form)
    const message = props.dialogType === 'add' ? '新增成功' : '修改成功'
    ElMessage.success(message)
    emit('success')
    handleClose()
  } catch (error) {
    console.log('表单验证失败:', error)
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}
</style>
