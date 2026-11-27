<template>
  <OrderFlowEditor :mode="mode" :order-id="Number(route.params.id)"/>
</template>

<script setup>
import {useRoute} from 'vue-router'
import OrderFlowEditor from '../components/OrderFlowEditor.vue'

defineProps({
  mode: {type: String, default: 'detail'}
})
const route = useRoute()
</script>
