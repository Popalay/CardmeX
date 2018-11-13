import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();

exports.sendPushWhenAddCardRequest = functions.firestore
  .document('requests/{requestId}')
  .onCreate(async (snap, context) => {

    const request = snap.data()
    const type = request.type
    const toUuid = request.toUserUuid
    const fromUuid = request.fromUserUuid

    return admin.auth().getUser(fromUuid)
      .then(async userFrom => {
        console.log('User has fetched');

        const notificationRef = admin.firestore().collection('notifications').doc()
        const title = userFrom.displayName + ' wants to add your card'
        const description = "Allow if you want to share card"

        const payload = {
          data: {
            notificationId: notificationRef.id,
            requestId: snap.id,
            title: title,
            description: description,
            icon: userFrom.photoURL,
          }
        };

        const options = {
          priority: "high",
          timeToLive: 60 * 60 * 24
        };

        const notification = {
          id: notificationRef.id,
          type: type,
          title: title,
          description: description,
          toUserUuid: toUuid,
          fromUser: {
            id: userFrom.uid,
            displayName: userFrom.displayName,
            photoUrl: userFrom.photoURL
          },
          createdDate: admin.firestore.Timestamp.now()
        };

        const saveNotificationPromise = notificationRef.set(notification)
        const sendPushPromise = admin.firestore().collection("token").doc(toUuid).get()
          .then(token => admin.messaging().sendToDevice(token.data().token, payload, options))

        return Promise.all([saveNotificationPromise, sendPushPromise])
          .then(results => {
            console.log('Notification has added');
            console.log('Push has sent');
          })
          .catch(error => console.log('Send notification error: ', error))
      });
  });

exports.saveCardToUserWhenAgree = functions.firestore
  .document('requests/{requestId}')
  .onUpdate(async (change, context) => {

    const request = change.after.data()
    const oldRequest = change.before.data()

    console.log('Request has updated!')
    if (request.allow !== oldRequest.allow) {

      console.log('Request allow has updated!')
      if (!request.allow) return

      console.log('Request allow is true!')
      const fromUserId = request.fromUserUuid
      const toUserId = request.toUserUuid

      const fetchUserPromise = admin.firestore().collection("users").doc(toUserId).get()
      const deleteRequestPromise = change.after.ref.delete()

      return fetchUserPromise.then(async userSnap => {
        console.log('User has fetched!')
        const userCardId = userSnap.data().cardId
        return admin.firestore().collection("cards").doc(userCardId).get().then(async cardSnap => {
          console.log('User card has fetched!')
          const card = cardSnap.data()
          const cardRef = admin.firestore().collection("cards").doc()
          card.id = cardRef.id
          card.userId = fromUserId
          card.updatedDate = admin.firestore.Timestamp.now()
          return cardRef.set(card).then(async () => {
            console.log('Card has added!')
            return deleteRequestPromise
              .then(() => console.log('Delete request succeeded!'))
              .catch(error => console.log('Delete request error: ', error))
          }).catch(error => console.log('Add card error: ', error))
        }).catch(error => console.log('Fetch user error: ', error))
      })
    }
    return null
  });