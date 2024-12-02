const { nanoid } = require("nanoid");
const dateFormat = require("dateformat");
const { Firestore } = require("@google-cloud/firestore");
const ImgUpload = require("./imgServices");

const firestore = new Firestore({
  databaseId: "articles",
  keyFilename: "serviceAccountKey.json",
  ignoreUndefinedProperties: true,
});

const articlesCollection = firestore.collection("articles");

const addArticleHandler = async (request, h) => {
  const { title, description, body, author, category } = request.payload;
  const imageFile = request.payload.image;

  if (!title) {
    return h
      .response({
        status: "fail",
        message: "Gagal menambahkan artikel. Mohon isi judul artikel",
      })
      .code(400);
  }

  if (!body) {
    return h
      .response({
        status: "fail",
        message: "Gagal menambahkan artikel. Mohon isi body artikel",
      })
      .code(400);
  }

  if (!category) {
    return h
      .response({
        status: "fail",
        message: "Gagal menambahkan artikel. Mohon isi category artikel",
      })
      .code(400);
  }

  let imageUrl = null;

  const id = category + "_" + nanoid(8);
  const createdAt = dateFormat(new Date(), "yyyymmdd-HHMMss");
  const updatedAt = createdAt;

  if (imageFile) {
    console.log("Gambar yang diterima:", imageFile.hapi);
    try {
      imageUrl = await ImgUpload.uploadImageToGcs(imageFile, id);
    } catch (error) {
      console.error("Gagal mengupload gambar: ", error);
      return h
        .response({
          status: "fail",
          message: "Gagal mengupload gambar",
        })
        .code(500);
    }
  }

  const newArticle = {
    id,
    title,
    author,
    body,
    imageUrl,
    category,
    description,
    createdAt,
    updatedAt,
  };

  try {
    await articlesCollection.doc(id).set(newArticle);

    const response = h.response({
      status: "success",
      message: "Artikel berhasil ditambahkan",
      data: {
        articleId: id,
      },
    });
    response.code(201);
    return response;
  } catch (error) {
    console.error("Gagal menambahkan artikel: ", error);

    const response = h.response({
      status: "fail",
      message: "Gagal menambahkan artikel",
      data: {
        articleId: id,
      },
    });
    response.code(500);
    return response;
  }
};

const getAllArticlesHandler = async (request, h) => {
  const { title, category, author, createdAt, updatedAt } = request.query;

  let query = articlesCollection;

  if (title) {
    query = query.where("title", "==", title);
  }

  if (category) {
    query = query.where("category", "==", category.toLowerCase());
  }

  if (author) {
    query = query.where("author", "==", author);
  }

  if (createdAt) {
    query = query.where("createdAt", ">=", new Date(createdAt));
  }

  if (updatedAt) {
    query = query.where("updatedAt", ">=", new Date(updatedAt));
  }

  try {
    const snapshot = await query.get();

    if (snapshot.empty) {
      return h
        .response({
          status: "fail",
          message: "Tidak ada artikel ditemukan",
        })
        .code(404);
    }

    const articles = snapshot.docs.map((doc) => ({
      id: doc.id,
      title: doc.data().title,
      category: doc.data().category,
      description: doc.data().description,
    }));

    return h
      .response({
        status: "success",
        data: {
          articles,
        },
      })
      .code(200);
  } catch (error) {
    console.error("Gagal mendapatkan artikel: ", error);

    return h
      .response({
        status: "fail",
        message: "Gagal mengambil artikel",
      })
      .code(500);
  }
};

const getArticleByIdHandler = async (request, h) => {
  const { id } = request.params;

  try {
    const articleRef = articlesCollection.doc(id);
    const doc = await articleRef.get();

    if (!doc.exists) {
      return h
        .response({
          status: "fail",
          message: "Artikel tidak ditemukan",
        })
        .code(404);
    }

    return {
      status: "success",
      data: {
        article: doc.data(),
      },
    };
  } catch (error) {
    console.error("Gagal mendapatkan artikel:", error);
    return h
      .response({
        status: "error",
        message: "Gagal mengambil artikel",
      })
      .code(500);
  }
};

const editArticleByIdHandler = async (request, h) => {
  const { id } = request.params;
  const { title, description, body, author, category } = request.payload;

  if (!title) {
    return h
      .response({
        status: "fail",
        message: "Gagal memperbarui artikel. Mohon isi judul artikel",
      })
      .code(400);
  }

  const updatedAt = dateFormat(new Date(), "yyyymmdd-HHMMss");

  try {
    const articleRef = articlesCollection.doc(id);
    const articleDoc = await articleRef.get();

    if (!articleDoc.exists) {
      return h
        .response({
          status: "fail",
          message: "Gagal memperbarui artikel. Id tidak ditemukan",
        })
        .code(404);
    }

    await articleRef.update({
      title,
      description,
      body,
      author,
      category,
      updatedAt,
    });

    const response = h.response({
      status: "success",
      message: "Artikel berhasil diperbarui",
    });
    response.code(200);
    return response;
  } catch (error) {
    console.error("Gagal memperbarui artikel: ", error);
    return h
      .response({
        status: "error",
        message: "Terjadi kesalahan saat memperbarui artikel",
      })
      .code(500);
  }
};

const deleteArticleByIdHandler = async (request, h) => {
  const { id } = request.params;

  try {
    const articleRef = articlesCollection.doc(id);
    const articleDoc = await articleRef.get();

    if (!articleDoc.exists) {
      return h
        .response({
          status: "fail",
          message: "Artikel gagal dihapus. Id tidak ditemukan",
        })
        .code(404);
    }

    await articleRef.delete();
    await ImgUpload.deleteImageFromGcs(id);

    const response = h.response({
      status: "success",
      message: "Artikel berhasil dihapus",
    });
    response.code(200);
    return response;
  } catch (error) {
    console.error("Gagal menghapus artikel: ", error);
    return h
      .response({
        status: "error",
        message: "Terjadi kesalahan saat menghapus artikel",
      })
      .code(500);
  }
};

module.exports = {
  addArticleHandler,
  getAllArticlesHandler,
  getArticleByIdHandler,
  editArticleByIdHandler,
  deleteArticleByIdHandler,
};
