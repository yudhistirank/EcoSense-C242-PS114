# Gunakan image resmi Node.js sebagai dasar
FROM node:20

# Tentukan direktori kerja di dalam container
WORKDIR /app

# Salin file package.json dan package-lock.json (jika ada) ke dalam direktori kerja
COPY package*.json ./

# Install dependencies yang terdaftar di package.json
RUN npm install

# Salin semua file aplikasi ke dalam container
COPY . .

# Expose port 8080 untuk aplikasi
EXPOSE 8080

# Tentukan perintah untuk menjalankan aplikasi
CMD ["npm", "start"]
