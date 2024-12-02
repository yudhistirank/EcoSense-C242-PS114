const { Storage } = require('@google-cloud/storage');

const storage = new Storage({
  keyFilename: 'serviceAccountKey.json',
});

const bucket = storage.bucket('images-ecosense');

const uploadImageToGcs = async (imageFile, id) => {
  let imageUrl = null;

  if (imageFile) {
    try {
      const fileName = `${id}`;
      const file = bucket.file(fileName);

      await new Promise((resolve, reject) => {
        const stream = file.createWriteStream({
          metadata: {
            contentType: imageFile.hapi.headers['content-type'],
          },
        });

        stream.on('finish', () => {
          imageUrl = `https://storage.googleapis.com/${bucket.name}/${fileName}`;
          resolve();
        });

        stream.on('error', (err) => {
          reject(err);
        });

        stream.end(imageFile._data);
      });
    } catch (error) {
      throw new Error('Gagal mengupload gambar: ' + error.message);
    }
  }

  return imageUrl;
};

const deleteImageFromGcs = async (id) => {
  try {
    const fileName = `${id}`;
    const file = bucket.file(fileName);

    // Menghapus file dari GCS
    await file.delete();

    console.log(`File dengan ID ${id} berhasil dihapus.`);
  } catch (error) {
    console.error('Gagal menghapus gambar:', error);
    throw new Error('Gagal menghapus gambar: ' + error.message);
  }
};

module.exports = { uploadImageToGcs, deleteImageFromGcs };
