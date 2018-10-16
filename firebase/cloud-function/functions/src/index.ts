import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase);

exports.sendPushWhenAddCardRequest = functions.firestore
  .document('requests/{requestId}')
  .onCreate((snap, context) => {

    const request = snap.data()
    const type = request.type
    const toUuid = request.toUserUuid
    const fromUuid = request.fromUserUuid

    return admin.auth().getUser(fromUuid)
      .then((userFrom) => {
        console.log('User has fetched');

        const notificationRef = admin.firestore().collection('notification').doc()
        const title = userFrom.displayName + ' wants to add your card'
        const description = "Allow if you want to share card"

        const payload = {
          data: {
            notificationId: notificationRef.id
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
          toUserUuid: toUuid,
          fromUserUuid: fromUuid
        };

        const saveNotificationPromise = notificationRef.set(notification)
        const sendPushPromise = admin.firestore().collection("token").doc(toUuid).get()
          .then(token => admin.messaging().sendToDevice(token.data().token, payload, options))

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