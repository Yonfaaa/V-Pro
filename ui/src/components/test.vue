<script>
import { defineComponent } from 'vue';
import axios from "axios";

// function getToken() {
//   // 这里是获取存储在 localStorage 或其他地方的 token 的逻辑
//   return localStorage.getItem('token');
// }

export default defineComponent({
  name: "TestComponent",
  data() {
    return {
      videoVisible: false,
      video: null,
      lvimage: [],
    };
  },
  methods: {
    async uploadsuccess() {
      this.videoVisible = true;
      let navigator = window.navigator.mediaDevices;
      await navigator.enumerateDevices();
      navigator.getUserMedia({
        video: {
          width: 500,
          height: 500,
        },
      }).then((mediaStream) => {
        this.video = document.querySelector("video");
        this.video.srcObject = mediaStream;
        this.video.onloadedmetadata = () => {
          this.video.play();
        };
      }).catch((err) => {
        console.log(err);
      });
    },
    clic() {
      const canvas = document.createElement("canvas");
      canvas.width = 200;
      canvas.height = 200;
      const ctx = canvas.getContext("2d");
      ctx.drawImage(this.video, 0, 0, canvas.width, canvas.height);
      const myImage = canvas.toDataURL("image/png");
      this.downloadImage(myImage, "captured_image.png");
      this.uploadImageToDB(myImage);
      this.videoVisible = false;
    },
    downloadImage(dataurl, filename) {
      // 创建一个链接元素
      const link = document.createElement('a');
      // 设置链接地址为图片的dataURL
      link.href = dataurl;
      // 设置下载的文件名
      link.download = filename;
      // 模拟点击链接
      link.click();
      // 移除链接元素
      link.remove();
    },
    uploadImageToDB(dataurl) {
      console.log('Sending image to server');
      axios.post('http://localhost:3000/upload', { image: dataurl })
          .then(response => {
            console.log('Image uploaded successfully', response.data);
          })
          .catch(error => {
            console.error('Error uploading image:', error);
          });
    }

  }
});
</script>

<template>
  <div>
    <el-image
        class="upload_add"
        v-for="url in lvimage"
        :key="url"
        :src="url"
    ></el-image>
    <img
        v-if="lvimage.length < 3"
        src="../assets/upload_add.png"
        class="upload_add"
        @click="uploadsuccess"
    />
    <el-dialog :visible.sync="videoVisible">
      <div class="flex flex-d a-center">
        <video id="video" style="width: 600px" class="mb10"></video>
        <el-button @click="clic" type="primary">拍摄</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 你可以在这里添加一些 CSS 样式 */
</style>
