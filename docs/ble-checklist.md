# Android BLE アプリ 実装チェックリスト

設計の詳細は [`ble-architecture.html`](./ble-architecture.html) を参照。
本ファイルは Section 7「クラス作成順」を進捗管理用にチェックリスト化したもの。

> 原則: 下から上に積む（モデル → Repository 抽象 → Data 実装 → ViewModel → Composable）。
> ViewModel は Repository を直接呼び出す（UseCase 層は作らない）。

---

## Phase 0 — 下準備

- [ ] **0-1** AndroidManifest に BLE 権限を宣言
  - [x] API 31+: `BLUETOOTH_SCAN` / `BLUETOOTH_CONNECT`
  - [x] API 30-: `ACCESS_FINE_LOCATION`（`android:maxSdkVersion="30"`）
  - [ ] API 30-: `BLUETOOTH` / `BLUETOOTH_ADMIN`
  - [ ] `<uses-feature android:name="android.hardware.bluetooth_le" required="true"/>`
- [ ] **0-2** `build.gradle.kts` にライブラリを追加
  - [ ] `androidx.lifecycle.viewmodel.compose`
  - [ ] `kotlinx-coroutines-android`
  - [ ] Navigation Compose
  - [ ] （任意）Hilt + KSP
- [ ] **0-3** パッケージのスケルトンを切る（空フォルダで OK）

---

## Phase 1 — BT 状態と権限の土台

- [x] **1-1** `BluetoothPermission` — API レベル別の権限定数
- [x] **1-2** `BluetoothPermissionChecker` — `hasScanPermission` / `hasConnectPermission`
- [x] **1-3** `BluetoothAdapterProvider` — `BluetoothManager` から adapter を取得、null 安全、BLE 非対応判定
- [x] **1-4** `BluetoothStateReceiver` — `ACTION_STATE_CHANGED` を `callbackFlow` で購読
- [ ] **1-5** `BluetoothStateRepository`（interface） — `isBluetoothSupported()` / `observeState()`
- [ ] **1-6** `BluetoothStateRepositoryImpl` — Receiver の Flow を domain 型に加工
- [ ] **1-7** `BluetoothPermissionRequester`（Composable） — `rememberLauncherForActivityResult` の薄いラッパ

> **動作確認** — MainActivity の仮 Composable で「BLE対応か」「BT ON か」「権限があるか」を Text 表示。OS 設定で BT を OFF→ON した時に表示が変わることまで確認する。

---

## Phase 2 — スキャン機能を MVVM 縦に1本通す

### 2-A. Domain

- [ ] **2-1** `BleDevice` — address, name。equals は address ベース
- [ ] **2-2** `BleScanResult` — `BleDevice + rssi`
- [ ] **2-3** `BleError`（sealed） — `PermissionDenied` / `BluetoothDisabled` / `ScanFailed(code)`
- [ ] **2-4** `BleScanRepository`（interface） — `startScan()` / `stopScan()` / `observeResults()`

### 2-B. Data

- [ ] **2-5** `ScanResultMapper` — `android.bluetooth.le.ScanResult` → `BleScanResult`
- [ ] **2-6** `ScanCallbackFlow` — `ScanCallback` を `callbackFlow` で Flow 化、`awaitClose` で stopScan
- [ ] **2-7** `BleScanner` — `BluetoothLeScanner` の start/stop ラッパ、`ScanFilter`/`ScanSettings`
- [ ] **2-8** `BleScanRepositoryImpl` — Scanner と Mapper を組み合わせて domain 型で公開

### 2-C. Presentation

- [ ] **2-9** `ScanUiState` — `{ isScanning, devices, error }`
- [ ] **2-10** `ScanViewModel` — `BleScanRepository` をコンストラクタで受け取り、`viewModelScope` で collect
- [ ] **2-11** `DeviceListItem`（Composable） — 1行表示、Preview で確認
- [ ] **2-12** `ScanScreen`（Composable） — ボタン + LazyColumn、権限要求もここから
- [ ] **2-13** MainActivity を `ScanScreen` に差し替え（NavHost は次フェーズ）

> **動作確認** — 実機で「スキャン開始 → 近くのデバイスが続々表示 → 停止で止まる」。実機必須（エミュレータは BLE 非対応）。

---

## Phase 3 — ナビゲーション導入

- [ ] **3-1** `Route`（sealed） — `Scan` / `DeviceDetail(address)`
- [ ] **3-2** `AppNavHost` — Scan を起点に登録、tap で `DeviceDetail` へ
- [ ] **3-3** `MainActivity` の `setContent` を `AppNavHost` に差し替え

> **動作確認** — タップで address を引数に詳細画面（仮表示）に遷移、戻れる。

---

## Phase 4 — 接続（GATT 接続まで）

### 4-A. Domain

- [ ] **4-1** `BleConnectionState`（sealed） — `Disconnected` / `Connecting` / `Connected` / `Disconnecting` / `Failed(reason)`
- [ ] **4-2** `BleConnectionRepository`（interface） — `connect(address)` / `disconnect()` / `observeConnectionState()`

### 4-B. Data

- [ ] **4-3** `GattConnectionHolder` — `BluetoothGatt` を1つだけ保持、disconnect で `close()` ⚠️ **先に作る**
- [ ] **4-4** `GattCallbackFlow` — sealed event Flow（まずは `ConnectionStateChange` / `ServicesDiscovered`）
- [ ] **4-5** `BleConnector` — `connectGatt` を呼んで Holder に保持、suspend 関数として包む
- [ ] **4-6** `BleConnectionRepositoryImpl` — 状態 Flow と connect/disconnect

### 4-C. Presentation

- [ ] **4-7** `DeviceDetailUiState` — 初期版は `{ connectionState }`
- [ ] **4-8** `DeviceDetailViewModel` — `SavedStateHandle` から address、`onCleared` で必ず `disconnect()`
- [ ] **4-9** `DeviceDetailScreen` — 接続状態表示の最小版

> ⚠️ **動作確認** — 接続できることより「画面を戻ったときに `close()` が呼ばれているか」をログで確認。複数回接続→離脱でリークしないか。

---

## Phase 5 — Service / Characteristic の検出

- [ ] **5-1** `BleService` / `BleCharacteristic` — UUID と性質フラグ（read/write/notify 可否）
- [ ] **5-2** `GattMapper` — SDK 型 → domain 型、properties のビットフラグ展開
- [ ] **5-3** `BleConnectionRepository` に `discoverServices()` を追加（interface と Impl 両方）
- [ ] **5-4** `DeviceDetailUiState` に `services` を追加
- [ ] **5-5** `DeviceDetailViewModel` で接続成功後に `discoverServices()` を呼ぶ
- [ ] **5-6** `ServiceList` / `CharacteristicRow`（Composable） — 展開可能なリスト

> **動作確認** — 接続後、Service と Characteristic の一覧が UUID で表示される。

---

## Phase 6 — Read / Write / Notify（GATT 操作）

> ⚠️ **BLE 実装の山場** — `GattOperationQueue` で操作を直列化する。並列で投げると静かに失敗する。

- [ ] **6-1** `GattOperationQueue` — Mutex か Channel で直列化、1操作 = 1 suspend
- [ ] **6-2** `GattCallbackFlow` 拡張 — `onCharacteristicRead` / `Write` / `Changed` を追加
- [ ] **6-3** `CharacteristicReader` — queue 経由で read、対応 callback を待つ
- [ ] **6-4** `CharacteristicWriter` — `WriteType`（with/without response）
- [ ] **6-5** `CharacteristicNotifier` — `setCharacteristicNotification` + CCCD 書き込み + 値変化 Flow
- [ ] **6-6** `BleConnectionRepository` に `readCharacteristic` / `writeCharacteristic` / `observeCharacteristic` を追加
- [ ] **6-7** `DeviceDetailViewModel` 拡張 — Read/Write/Notify を Repository 経由で呼ぶ
- [ ] **6-8** `CharacteristicRow` に Read/Write/Notify ボタンを追加

> ⚠️ **動作確認** — Read 連打で落ちない／結果が混ざらない、Write 応答が返る、Notify 有効化で値変化が UI に流れる。Battery Service (`0x180F`) で検証推奨。

---

## Phase 7 — 仕上げ（任意）

- [ ] **7-1** `UuidUtils` / `HexConverter` — UI 表示用
- [ ] **7-2** `BluetoothViewerApplication` + Hilt Module — DI 導入
- [ ] **7-3** `BleConnectionService` — フォアグラウンドサービス（画面離れても接続維持したい時のみ）
- [ ] **7-4** `LoadingIndicator` / `ErrorMessage` — 共通 UI

---

## UseCase 抽出トリガ（随時チェック）

以下のいずれかが発火したら、その操作だけ UseCase を切り出す:

- [ ] ① 複数 Repository を1操作に束ねたい（例: BT状態確認 → 権限確認 → スキャン開始）
- [ ] ② 同じ orchestration が2つ以上の ViewModel から呼ばれる
- [ ] ③ ViewModel が肥大化／pure Kotlin で単体テストしたい

> 抽出のコツ — 全部一気に UseCase 化しない。1メソッドだけ Repository → UseCase に置き換える PR を作る、というレベルで小さく入れる。
