const express = require('express');
const mysql = require('mysql');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;
const cors = require('cors');

// 配置CORS
app.use(cors({
    origin: '*' // 允许来自8080端口的客户端请求
}));
// MySQL连接配置
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',  // 替换为你的用户名
    password: '123456',  // 替换为你的密码
    database: 'ry_pro_flowable'  // 替换为你的数据库名
});

// 连接到数据库
db.connect(err => {
    if (err) {
        console.error('Error connecting to MySQL:', err);
        return;
    }
    console.log('Connected to MySQL');
});

app.use(bodyParser.json({ limit: '50mb' }));
app.use(bodyParser.urlencoded({ extended: true, limit: '50mb' }));

// 上传图片数据
app.post('/upload', (req, res) => {
    const sql = 'INSERT INTO images (dataUrl) VALUES (?)';
    db.query(sql, [req.body.image], (err, result) => {
        if (err) {
            console.error('Error saving image:', err);
            res.status(500).send('Error saving image to database');
            return;
        }
        console.log('Image saved to database', result);
        res.send('Image saved to database');
    });
});


app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
