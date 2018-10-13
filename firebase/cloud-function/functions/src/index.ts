import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase);

exports.sendPushWhenAddCardRequest = functions.firestore
  .document('request/{requestId}')
  .onCreate((snap, context) => {

    const request = snap.data()
    const type = request.type
    const cardId = request.cardId
    const toUuid = request.toUserUuid
    const fromUuid = request.fromUserUuid

    return admin.auth().getUser(fromUuid)
      .then((userFrom) => {
        console.log('User has fetched');

        const notificationRef = admin.firestore().collection('notification').doc()
        const title = userFrom.displayName + ' wants add your card'
        const description = "Description"

        const payload = {
          data: {
            //token: token
          },
          notification: {
            title: title,
            body: description,
            icon: userFrom.photoURL,
            sound: "default"
          },
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
          cardId: cardId
        };

        const saveNotificationPromise = notificationRef.set(notification)
        const sendPushPromise = admin.firestore().collection("token").where('userUuid', '==', toUuid).limit(1).get()
          .then(token => admin.messaging().sendToDevice(token.docs[0].data().token, payload, options))

        return Promise.all([saveNotificationPromise, sendPushPromise])
          .then(results => {
            console.log('Notification has added');
            console.log('Push has sent');
          })
          .catch(error => {
            console.log('Send notification error: ', error);
          });
      });
  });