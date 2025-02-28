package com.petpal.swimmer_seller.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.petpal.swimmer_seller.data.model.Product

class ProductRepository {
    // 상품 정보 저장
    fun addProduct(product: Product, callback: (Task<Void>) -> Unit) {
        // Product 객체 삽입 후 랜덤 세팅된 key값을 Product객체 내 productUid로 저장
        val productsDataRef = FirebaseDatabase.getInstance().getReference("products")
        val pushedRef = productsDataRef.push()
        product.productUid = pushedRef.key!!
        pushedRef.setValue(product).addOnCompleteListener(callback)
    }

    // 상품 정보 수정
    fun modifyProduct(product: Product, isNewImage: Boolean, callback: (Task<Void>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val productsDataRef = database.getReference("products")

        productsDataRef.orderByChild("productUid").equalTo(product.productUid).get()
            .addOnCompleteListener {
                for (dataSnapshot in it.result.children) {
                    // 수정 가능한 항목만 값 수정
                    // code, sellerIdx, orderCount, regDate 등은 고정 값 / reviewList는 판매자가 조작할 수 없는 값
                    if (isNewImage) {
                        dataSnapshot.ref.child("mainImage").setValue(product.mainImage)
                        dataSnapshot.ref.child("descriptionImage")
                            .setValue(product.descriptionImage)
                    }
                    dataSnapshot.ref.child("name").setValue(product.name)
                    dataSnapshot.ref.child("price").setValue(product.price)
                    dataSnapshot.ref.child("description").setValue(product.description)
                    dataSnapshot.ref.child("sizeList").setValue(product.sizeList)
                    dataSnapshot.ref.child("colorList").setValue(product.colorList)
                    dataSnapshot.ref.child("hashTag").setValue(product.hashTag)
                    dataSnapshot.ref.child("category").setValue(product.category)
                        .addOnCompleteListener(callback)
                }
            }
    }

    // 상품 정보 삭제
    fun deleteProduct(productUid: String, callback: (Task<Void>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val productsDataRef = database.getReference("products")

        productsDataRef.orderByChild("productUid").equalTo(productUid).get()
            .addOnCompleteListener {
                for (dataSnapshot in it.result.children) {
                    dataSnapshot.ref.removeValue().addOnCompleteListener(callback)
                }
            }
    }

    // 특정 판매자가 등록한 상품 목록 가져오기
    fun getProductListBySellerIdx(sellerUid: String, callback: (Task<DataSnapshot>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val productsDataRef = database.getReference("products")

        productsDataRef.orderByChild("sellerUid").equalTo(sellerUid)
            .ref.orderByChild("regDate").get().addOnCompleteListener(callback)
    }

    // 특정 파일명으로 이미지 업로드
    fun uploadImage(
        uploadUri: Uri,
        fileName: String,
        callback: (Task<UploadTask.TaskSnapshot>) -> Unit
    ) {
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.reference.child(fileName)
        imageRef.putFile(uploadUri).addOnCompleteListener(callback)
    }

    // 특정 파일명의 이미지 다운로드
    fun downloadImage(fileName: String, callback: (Task<Uri>) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.reference.child(fileName)
        imageRef.downloadUrl.addOnCompleteListener(callback)
    }
}