<?php
namespace Tubewarder;
use \Exception as Exception;
require('TubewarderClient.php');

class TubewarderRestClient extends TubewarderClient {
    /**
     * @param SendRequest $request
     * @return SendResponse
     * @throws Exception
     */
    public function send(SendRequest $request) {
        $ch = curl_init($this->getUri().'rs/send');
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($request->getObject()));
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        $result = curl_exec($ch);
        if ($result === false) {
            throw new Exception('Could not connect to server');
        }
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        if ($status != 200) {
            throw new Exception('Invalid http code '.$status);
        }
        $json = json_decode($result, true);
        return SendResponse::createFromObject($json);
    }
}
?>