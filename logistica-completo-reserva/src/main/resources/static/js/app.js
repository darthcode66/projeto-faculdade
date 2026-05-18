// ============================
// TransLog - Frontend
// ============================

const API = '';
let currentUser = null;

// ── Auth ──────────────────────────────────────────────────────────────

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const senha = document.getElementById('loginSenha').value;

    try {
        const res = await fetch(API + '/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (res.ok) {
            currentUser = await res.json();
            localStorage.setItem('user', JSON.stringify(currentUser));
            showApp();
        } else {
            const err = await res.json();
            showError('loginError', err.erro || 'Erro ao fazer login');
        }
    } catch (err) {
        showError('loginError', 'Erro de conexao com o servidor');
    }
    return false;
}

function showApp() {
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('appPage').style.display = 'flex';
    document.getElementById('userName').textContent = currentUser.nome + ' (' + currentUser.perfil + ')';
    loadDashboard();
}

function logout() {
    currentUser = null;
    localStorage.removeItem('user');
    document.getElementById('loginPage').style.display = 'flex';
    document.getElementById('appPage').style.display = 'none';
}

function showError(elementId, msg) {
    const el = document.getElementById(elementId);
    el.textContent = msg;
    el.style.display = 'block';
    setTimeout(() => el.style.display = 'none', 4000);
}

// ── Navigation ────────────────────────────────────────────────────────

const sectionTitles = {
    dashboard: 'Dashboard',
    veiculos: 'Veiculos',
    rotas: 'Rotas',
    cargas: 'Cargas',
    clientes: 'Clientes',
    motoristas: 'Motoristas',
    viagens: 'Viagens',
    frete: 'Calcular Frete',
    historico: 'Historico de Fretes'
};

function showSection(name) {
    document.querySelectorAll('.page-section').forEach(s => s.classList.remove('active'));
    document.getElementById('section-' + name).classList.add('active');
    document.getElementById('pageTitle').textContent = sectionTitles[name] || name;

    document.querySelectorAll('.sidebar-nav a').forEach(a => a.classList.remove('active'));
    const link = document.querySelector('[data-section="' + name + '"]');
    if (link) link.classList.add('active');

    // Load data
    switch (name) {
        case 'dashboard': loadDashboard(); break;
        case 'veiculos': loadVeiculos(); break;
        case 'rotas': loadRotas(); break;
        case 'cargas': loadCargas(); break;
        case 'clientes': loadClientes(); break;
        case 'motoristas': loadMotoristas(); break;
        case 'viagens': loadViagens(); break;
        case 'historico': loadFretes(); break;
    }
}

// ── Modal ─────────────────────────────────────────────────────────────

function openModal(id) { document.getElementById(id).classList.add('active'); }
function closeModal(id) { document.getElementById(id).classList.remove('active'); }

// ── Dashboard ─────────────────────────────────────────────────────────

async function loadDashboard() {
    try {
        const res = await fetch(API + '/api/relatorios/dashboard');
        const d = await res.json();

        document.getElementById('dashboardCards').innerHTML = `
            <div class="stat-card primary">
                <div class="card-icon"><i data-lucide="truck"></i></div>
                <div class="card-value">${d.totalVeiculos}</div>
                <div class="card-label">Total de Veiculos</div>
            </div>
            <div class="stat-card success">
                <div class="card-icon"><i data-lucide="circle-check"></i></div>
                <div class="card-value">${d.veiculosDisponiveis}</div>
                <div class="card-label">Veiculos Disponiveis</div>
            </div>
            <div class="stat-card warning">
                <div class="card-icon"><i data-lucide="navigation"></i></div>
                <div class="card-value">${d.viagensEmTransito}</div>
                <div class="card-label">Em Transito</div>
            </div>
            <div class="stat-card primary">
                <div class="card-icon"><i data-lucide="route"></i></div>
                <div class="card-value">${d.totalRotas}</div>
                <div class="card-label">Rotas Cadastradas</div>
            </div>
            <div class="stat-card success">
                <div class="card-icon"><i data-lucide="package"></i></div>
                <div class="card-value">${d.totalCargas}</div>
                <div class="card-label">Cargas Cadastradas</div>
            </div>
            <div class="stat-card primary">
                <div class="card-icon"><i data-lucide="building-2"></i></div>
                <div class="card-value">${d.totalClientes}</div>
                <div class="card-label">Clientes Cadastrados</div>
            </div>
            <div class="stat-card success">
                <div class="card-icon"><i data-lucide="user-round"></i></div>
                <div class="card-value">${d.motoristasAtivos}</div>
                <div class="card-label">Motoristas Ativos</div>
            </div>
            <div class="stat-card primary">
                <div class="card-icon"><i data-lucide="map-pin"></i></div>
                <div class="card-value">${d.totalViagens}</div>
                <div class="card-label">Total de Viagens</div>
            </div>
            <div class="stat-card success">
                <div class="card-icon"><i data-lucide="circle-check-big"></i></div>
                <div class="card-value">${d.viagensEntregues}</div>
                <div class="card-label">Entregas Concluidas</div>
            </div>
            <div class="stat-card warning">
                <div class="card-icon"><i data-lucide="dollar-sign"></i></div>
                <div class="card-value">R$ ${formatMoney(d.receitaTotal)}</div>
                <div class="card-label">Receita Total (Fretes)</div>
            </div>
        `;
        lucide.createIcons();
    } catch (e) {
        console.error('Erro ao carregar dashboard:', e);
    }
}

// ── Veiculos ──────────────────────────────────────────────────────────

async function loadVeiculos() {
    const res = await fetch(API + '/api/veiculos');
    const veiculos = await res.json();

    document.getElementById('veiculosTable').innerHTML = veiculos.map(v => `
        <tr>
            <td><strong>${v.placa}</strong></td>
            <td>${v.modelo}</td>
            <td>${v.marca}</td>
            <td>${formatTipoVeiculo(v.tipo)}</td>
            <td>${formatNumber(v.capacidadeKg)} kg</td>
            <td>${badgeStatus(v.status)}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editarVeiculo(${v.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deletarVeiculo(${v.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

async function salvarVeiculo() {
    const id = document.getElementById('veiculoId').value;
    const data = {
        placa: document.getElementById('veiculoPlaca').value,
        modelo: document.getElementById('veiculoModelo').value,
        marca: document.getElementById('veiculoMarca').value,
        anoFabricacao: parseInt(document.getElementById('veiculoAno').value),
        capacidadeKg: parseFloat(document.getElementById('veiculoCapacidade').value),
        tipo: document.getElementById('veiculoTipo').value,
        status: document.getElementById('veiculoStatus').value
    };

    const url = id ? API + '/api/veiculos/' + id : API + '/api/veiculos';
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    closeModal('veiculoModal');
    clearVeiculoForm();
    loadVeiculos();
}

async function editarVeiculo(id) {
    const res = await fetch(API + '/api/veiculos/' + id);
    const v = await res.json();

    document.getElementById('veiculoId').value = v.id;
    document.getElementById('veiculoPlaca').value = v.placa;
    document.getElementById('veiculoModelo').value = v.modelo;
    document.getElementById('veiculoMarca').value = v.marca;
    document.getElementById('veiculoAno').value = v.anoFabricacao;
    document.getElementById('veiculoCapacidade').value = v.capacidadeKg;
    document.getElementById('veiculoTipo').value = v.tipo;
    document.getElementById('veiculoStatus').value = v.status;
    document.getElementById('veiculoModalTitle').textContent = 'Editar Veiculo';

    openModal('veiculoModal');
}

async function deletarVeiculo(id) {
    if (confirm('Tem certeza que deseja excluir este veiculo?')) {
        await fetch(API + '/api/veiculos/' + id, { method: 'DELETE' });
        loadVeiculos();
    }
}

function clearVeiculoForm() {
    document.getElementById('veiculoId').value = '';
    document.getElementById('veiculoPlaca').value = '';
    document.getElementById('veiculoModelo').value = '';
    document.getElementById('veiculoMarca').value = '';
    document.getElementById('veiculoAno').value = '2024';
    document.getElementById('veiculoCapacidade').value = '';
    document.getElementById('veiculoModalTitle').textContent = 'Novo Veiculo';
}

// ── Rotas ─────────────────────────────────────────────────────────────

async function loadRotas() {
    const res = await fetch(API + '/api/rotas');
    const rotas = await res.json();

    document.getElementById('rotasTable').innerHTML = rotas.map(r => `
        <tr>
            <td>${r.origem}</td>
            <td>${r.destino}</td>
            <td>${formatNumber(r.distanciaKm)} km</td>
            <td>${r.tempoEstimadoHoras}h</td>
            <td>R$ ${formatMoney(r.custoPedagio)}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editarRota(${r.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deletarRota(${r.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

async function salvarRota() {
    const id = document.getElementById('rotaId').value;
    const data = {
        origem: document.getElementById('rotaOrigem').value,
        destino: document.getElementById('rotaDestino').value,
        distanciaKm: parseFloat(document.getElementById('rotaDistancia').value),
        tempoEstimadoHoras: parseFloat(document.getElementById('rotaTempo').value),
        pedagios: document.getElementById('rotaPedagios').value,
        custoPedagio: parseFloat(document.getElementById('rotaCustoPedagio').value)
    };

    const url = id ? API + '/api/rotas/' + id : API + '/api/rotas';
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    closeModal('rotaModal');
    clearRotaForm();
    loadRotas();
}

async function editarRota(id) {
    const res = await fetch(API + '/api/rotas/' + id);
    const r = await res.json();

    document.getElementById('rotaId').value = r.id;
    document.getElementById('rotaOrigem').value = r.origem;
    document.getElementById('rotaDestino').value = r.destino;
    document.getElementById('rotaDistancia').value = r.distanciaKm;
    document.getElementById('rotaTempo').value = r.tempoEstimadoHoras;
    document.getElementById('rotaPedagios').value = r.pedagios || '';
    document.getElementById('rotaCustoPedagio').value = r.custoPedagio;
    document.getElementById('rotaModalTitle').textContent = 'Editar Rota';

    openModal('rotaModal');
}

async function deletarRota(id) {
    if (confirm('Tem certeza que deseja excluir esta rota?')) {
        await fetch(API + '/api/rotas/' + id, { method: 'DELETE' });
        loadRotas();
    }
}

function clearRotaForm() {
    document.getElementById('rotaId').value = '';
    document.getElementById('rotaOrigem').value = '';
    document.getElementById('rotaDestino').value = '';
    document.getElementById('rotaDistancia').value = '';
    document.getElementById('rotaTempo').value = '';
    document.getElementById('rotaPedagios').value = '';
    document.getElementById('rotaCustoPedagio').value = '0';
    document.getElementById('rotaModalTitle').textContent = 'Nova Rota';
}

// ── Cargas ─────────────────────────────────────────────────────────────

async function loadCargas() {
    const res = await fetch(API + '/api/cargas');
    const cargas = await res.json();

    document.getElementById('cargasTable').innerHTML = cargas.map(c => `
        <tr>
            <td>${c.descricao}</td>
            <td>${formatNumber(c.pesoKg)} kg</td>
            <td>${c.volumeM3} m3</td>
            <td>${formatTipoCarga(c.tipo)}</td>
            <td>${c.fragil ? '<span class="badge badge-warning">Sim</span>' : 'Nao'}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editarCarga(${c.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deletarCarga(${c.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

async function salvarCarga() {
    const id = document.getElementById('cargaId').value;
    const data = {
        descricao: document.getElementById('cargaDescricao').value,
        pesoKg: parseFloat(document.getElementById('cargaPeso').value),
        volumeM3: parseFloat(document.getElementById('cargaVolume').value),
        tipo: document.getElementById('cargaTipo').value,
        fragil: document.getElementById('cargaFragil').checked,
        perigosa: document.getElementById('cargaPerigosa').checked
    };

    const url = id ? API + '/api/cargas/' + id : API + '/api/cargas';
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    closeModal('cargaModal');
    clearCargaForm();
    loadCargas();
}

async function editarCarga(id) {
    const res = await fetch(API + '/api/cargas/' + id);
    const c = await res.json();

    document.getElementById('cargaId').value = c.id;
    document.getElementById('cargaDescricao').value = c.descricao;
    document.getElementById('cargaPeso').value = c.pesoKg;
    document.getElementById('cargaVolume').value = c.volumeM3;
    document.getElementById('cargaTipo').value = c.tipo;
    document.getElementById('cargaFragil').checked = c.fragil;
    document.getElementById('cargaPerigosa').checked = c.perigosa;
    document.getElementById('cargaModalTitle').textContent = 'Editar Carga';

    openModal('cargaModal');
}

async function deletarCarga(id) {
    if (confirm('Tem certeza que deseja excluir esta carga?')) {
        await fetch(API + '/api/cargas/' + id, { method: 'DELETE' });
        loadCargas();
    }
}

function clearCargaForm() {
    document.getElementById('cargaId').value = '';
    document.getElementById('cargaDescricao').value = '';
    document.getElementById('cargaPeso').value = '';
    document.getElementById('cargaVolume').value = '';
    document.getElementById('cargaFragil').checked = false;
    document.getElementById('cargaPerigosa').checked = false;
    document.getElementById('cargaModalTitle').textContent = 'Nova Carga';
}

// ── Clientes ──────────────────────────────────────────────────────────

async function loadClientes() {
    const res = await fetch(API + '/api/clientes');
    const clientes = await res.json();

    document.getElementById('clientesTable').innerHTML = clientes.map(c => `
        <tr>
            <td><strong>${c.nome}</strong></td>
            <td>${c.cnpj || '-'}</td>
            <td>${c.cidade || '-'}</td>
            <td>${c.telefone || '-'}</td>
            <td>${c.email || '-'}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editarCliente(${c.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deletarCliente(${c.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

async function salvarCliente() {
    const id = document.getElementById('clienteId').value;
    const data = {
        nome: document.getElementById('clienteNome').value,
        cnpj: document.getElementById('clienteCnpj').value,
        cidade: document.getElementById('clienteCidade').value,
        telefone: document.getElementById('clienteTelefone').value,
        email: document.getElementById('clienteEmail').value
    };

    const url = id ? API + '/api/clientes/' + id : API + '/api/clientes';
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    closeModal('clienteModal');
    clearClienteForm();
    loadClientes();
}

async function editarCliente(id) {
    const res = await fetch(API + '/api/clientes/' + id);
    const c = await res.json();

    document.getElementById('clienteId').value = c.id;
    document.getElementById('clienteNome').value = c.nome;
    document.getElementById('clienteCnpj').value = aplicarMascaraCnpj(c.cnpj || '');
    document.getElementById('clienteCidade').value = c.cidade || '';
    document.getElementById('clienteTelefone').value = aplicarMascaraTelefone(c.telefone || '');
    document.getElementById('clienteEmail').value = c.email || '';
    document.getElementById('clienteModalTitle').textContent = 'Editar Cliente';

    openModal('clienteModal');
}

async function deletarCliente(id) {
    if (confirm('Tem certeza que deseja excluir este cliente?')) {
        await fetch(API + '/api/clientes/' + id, { method: 'DELETE' });
        loadClientes();
    }
}

function clearClienteForm() {
    document.getElementById('clienteId').value = '';
    document.getElementById('clienteNome').value = '';
    document.getElementById('clienteCnpj').value = '';
    document.getElementById('clienteCidade').value = '';
    document.getElementById('clienteTelefone').value = '';
    document.getElementById('clienteEmail').value = '';
    document.getElementById('clienteModalTitle').textContent = 'Novo Cliente';
}

// ── Motoristas ────────────────────────────────────────────────────────

async function loadMotoristas() {
    const res = await fetch(API + '/api/motoristas');
    const motoristas = await res.json();

    document.getElementById('motoristasTable').innerHTML = motoristas.map(m => `
        <tr>
            <td><strong>${m.nome}</strong></td>
            <td>${m.cnh}</td>
            <td>${m.categoriaCnh || '-'}</td>
            <td>${m.validadeCnh ? new Date(m.validadeCnh).toLocaleDateString('pt-BR') : '-'}</td>
            <td>${m.telefone || '-'}</td>
            <td>${m.ativo ? '<span class="badge badge-success">Ativo</span>' : '<span class="badge badge-secondary">Inativo</span>'}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editarMotorista(${m.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deletarMotorista(${m.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

async function salvarMotorista() {
    const id = document.getElementById('motoristaId').value;
    const data = {
        nome: document.getElementById('motoristaNome').value,
        cnh: document.getElementById('motoristaCnh').value,
        categoriaCnh: document.getElementById('motoristaCategoria').value,
        validadeCnh: document.getElementById('motoristaValidade').value || null,
        telefone: document.getElementById('motoristaTelefone').value,
        ativo: document.getElementById('motoristaAtivo').checked
    };

    const url = id ? API + '/api/motoristas/' + id : API + '/api/motoristas';
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    closeModal('motoristaModal');
    clearMotoristaForm();
    loadMotoristas();
}

async function editarMotorista(id) {
    const res = await fetch(API + '/api/motoristas/' + id);
    const m = await res.json();

    document.getElementById('motoristaId').value = m.id;
    document.getElementById('motoristaNome').value = m.nome;
    document.getElementById('motoristaCnh').value = m.cnh;
    document.getElementById('motoristaCategoria').value = m.categoriaCnh || 'E';
    document.getElementById('motoristaValidade').value = m.validadeCnh || '';
    document.getElementById('motoristaTelefone').value = aplicarMascaraTelefone(m.telefone || '');
    document.getElementById('motoristaAtivo').checked = m.ativo;
    document.getElementById('motoristaModalTitle').textContent = 'Editar Motorista';

    openModal('motoristaModal');
}

async function deletarMotorista(id) {
    if (confirm('Tem certeza que deseja excluir este motorista?')) {
        await fetch(API + '/api/motoristas/' + id, { method: 'DELETE' });
        loadMotoristas();
    }
}

function clearMotoristaForm() {
    document.getElementById('motoristaId').value = '';
    document.getElementById('motoristaNome').value = '';
    document.getElementById('motoristaCnh').value = '';
    document.getElementById('motoristaCategoria').value = 'E';
    document.getElementById('motoristaValidade').value = '';
    document.getElementById('motoristaTelefone').value = '';
    document.getElementById('motoristaAtivo').checked = true;
    document.getElementById('motoristaModalTitle').textContent = 'Novo Motorista';
}

// ── Viagens ───────────────────────────────────────────────────────────

async function loadViagens() {
    const res = await fetch(API + '/api/viagens');
    const viagens = await res.json();

    document.getElementById('viagensTable').innerHTML = viagens.map(v => `
        <tr>
            <td>${v.motorista ? v.motorista.nome : '-'}</td>
            <td>${v.veiculo ? v.veiculo.placa + ' - ' + v.veiculo.modelo : '-'}</td>
            <td>${v.rota ? v.rota.origem + ' > ' + v.rota.destino : '-'}</td>
            <td>${v.carga ? v.carga.descricao : '-'}</td>
            <td>${formatDate(v.dataPartida)}</td>
            <td>${badgeViagem(v.status)}</td>
            <td>
                ${v.status === 'PLANEJADA' ? `<button class="btn btn-warning btn-sm" onclick="atualizarStatusViagem(${v.id}, 'EM_TRANSITO')">Iniciar</button>` : ''}
                ${v.status === 'EM_TRANSITO' ? `<button class="btn btn-success btn-sm" onclick="atualizarStatusViagem(${v.id}, 'ENTREGUE')">Entregar</button>` : ''}
                ${v.status !== 'ENTREGUE' && v.status !== 'CANCELADA' ? `<button class="btn btn-danger btn-sm" onclick="atualizarStatusViagem(${v.id}, 'CANCELADA')">Cancelar</button>` : ''}
            </td>
        </tr>
    `).join('');
}

async function openNovaViagem() {
    const [veiculos, rotas, cargas, motoristas, clientes] = await Promise.all([
        fetch(API + '/api/veiculos/disponiveis').then(r => r.json()),
        fetch(API + '/api/rotas').then(r => r.json()),
        fetch(API + '/api/cargas').then(r => r.json()),
        fetch(API + '/api/motoristas/ativos').then(r => r.json()),
        fetch(API + '/api/clientes').then(r => r.json())
    ]);

    document.getElementById('viagemMotorista').innerHTML = motoristas.map(m =>
        `<option value="${m.id}">${m.nome} - CNH ${m.cnh}</option>`
    ).join('');

    document.getElementById('viagemCliente').innerHTML =
        '<option value="">-- Sem cliente --</option>' +
        clientes.map(c => `<option value="${c.id}">${c.nome} (${c.cidade})</option>`).join('');

    document.getElementById('viagemVeiculo').innerHTML = veiculos.map(v =>
        `<option value="${v.id}">${v.placa} - ${v.modelo} (${formatNumber(v.capacidadeKg)} kg)</option>`
    ).join('');

    document.getElementById('viagemRota').innerHTML = rotas.map(r =>
        `<option value="${r.id}">${r.origem} > ${r.destino} (${r.distanciaKm} km)</option>`
    ).join('');

    document.getElementById('viagemCarga').innerHTML = cargas.map(c =>
        `<option value="${c.id}">${c.descricao} (${formatNumber(c.pesoKg)} kg)</option>`
    ).join('');

    openModal('viagemModal');
}

async function salvarViagem() {
    const clienteId = document.getElementById('viagemCliente').value;
    const data = {
        motoristaId: document.getElementById('viagemMotorista').value,
        clienteId: clienteId || null,
        veiculoId: document.getElementById('viagemVeiculo').value,
        rotaId: document.getElementById('viagemRota').value,
        cargaId: document.getElementById('viagemCarga').value,
        dataPartida: document.getElementById('viagemPartida').value,
        dataChegadaPrevista: document.getElementById('viagemChegada').value,
        observacoes: document.getElementById('viagemObs').value
    };

    await fetch(API + '/api/viagens', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    closeModal('viagemModal');
    loadViagens();
}

async function atualizarStatusViagem(id, status) {
    await fetch(API + '/api/viagens/' + id + '/status', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status })
    });
    loadViagens();
}

// ── Frete ─────────────────────────────────────────────────────────────

async function simularFrete() {
    await calcularFreteInterno('/api/fretes/simular');
}

async function calcularFrete() {
    await calcularFreteInterno('/api/fretes/calcular');
}

async function calcularFreteInterno(endpoint) {
    const data = {
        distanciaKm: parseFloat(document.getElementById('freteDistancia').value),
        pesoKg: parseFloat(document.getElementById('fretePeso').value),
        tipoCarga: document.getElementById('freteTipo').value,
        custoPedagio: parseFloat(document.getElementById('fretePedagio').value),
        margemLucro: parseFloat(document.getElementById('freteMargem').value)
    };

    if (!data.distanciaKm || !data.pesoKg) {
        alert('Preencha a distancia e o peso da carga');
        return;
    }

    const res = await fetch(API + endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    const f = await res.json();

    document.getElementById('freteResultado').innerHTML = `
        <div class="result-item"><span class="label">Distancia</span><span class="value">${formatNumber(f.distanciaKm)} km</span></div>
        <div class="result-item"><span class="label">Peso</span><span class="value">${formatNumber(f.pesoKg)} kg</span></div>
        <div class="result-item"><span class="label">Tipo de Carga</span><span class="value">${formatTipoCarga(f.tipoCarga)}</span></div>
        <hr style="border:none;border-top:1px solid #f0f0f0;margin:8px 0">
        <div class="result-item"><span class="label">Combustivel</span><span class="value">R$ ${formatMoney(f.custoCombustivel)}</span></div>
        <div class="result-item"><span class="label">Manutencao</span><span class="value">R$ ${formatMoney(f.custoManutencao)}</span></div>
        <div class="result-item"><span class="label">Pedagio</span><span class="value">R$ ${formatMoney(f.custoPedagio)}</span></div>
        <div class="result-item"><span class="label">Custo Fixo</span><span class="value">R$ ${formatMoney(f.custoFixo)}</span></div>
        <div class="result-item"><span class="label">Custo Variavel</span><span class="value">R$ ${formatMoney(f.custoVariavel)}</span></div>
        <div class="result-item"><span class="label">Margem de Lucro</span><span class="value">${f.margemLucro}%</span></div>
        <div class="result-total"><span>VALOR TOTAL</span><span>R$ ${formatMoney(f.valorTotal)}</span></div>
    `;
}

// ── Historico Fretes ──────────────────────────────────────────────────

async function loadFretes() {
    const res = await fetch(API + '/api/fretes');
    const fretes = await res.json();

    document.getElementById('fretesTable').innerHTML = fretes.map(f => `
        <tr>
            <td>#${f.id}</td>
            <td>${formatNumber(f.distanciaKm)} km</td>
            <td>${formatNumber(f.pesoKg)} kg</td>
            <td>${formatTipoCarga(f.tipoCarga)}</td>
            <td>R$ ${formatMoney(f.custoFixo + f.custoVariavel)}</td>
            <td>${f.margemLucro}%</td>
            <td><strong>R$ ${formatMoney(f.valorTotal)}</strong></td>
            <td>
                <button class="btn btn-danger btn-sm" onclick="deletarFrete(${f.id})">Excluir</button>
            </td>
        </tr>
    `).join('');
}

async function deletarFrete(id) {
    if (confirm('Excluir este registro de frete?')) {
        await fetch(API + '/api/fretes/' + id, { method: 'DELETE' });
        loadFretes();
    }
}

// ── Helpers ───────────────────────────────────────────────────────────

function formatMoney(v) {
    if (v == null) return '0,00';
    return v.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function formatNumber(v) {
    if (v == null) return '0';
    return v.toLocaleString('pt-BR');
}

function formatDate(d) {
    if (!d) return '-';
    return new Date(d).toLocaleString('pt-BR');
}

function formatTipoVeiculo(t) {
    const map = {
        CAMINHAO_PEQUENO: 'Caminhao Pequeno',
        CAMINHAO_MEDIO: 'Caminhao Medio',
        CAMINHAO_GRANDE: 'Caminhao Grande',
        CARRETA: 'Carreta',
        VAN: 'Van'
    };
    return map[t] || t;
}

function formatTipoCarga(t) {
    const map = {
        GERAL: 'Geral',
        GRANEL: 'Granel',
        REFRIGERADA: 'Refrigerada',
        PERIGOSA: 'Perigosa',
        FRAGIL: 'Fragil',
        VIVA: 'Carga Viva'
    };
    return map[t] || t || 'Geral';
}

function badgeStatus(s) {
    const map = {
        DISPONIVEL: '<span class="badge badge-success">Disponivel</span>',
        EM_VIAGEM: '<span class="badge badge-warning">Em Viagem</span>',
        MANUTENCAO: '<span class="badge badge-danger">Manutencao</span>'
    };
    return map[s] || s;
}

function badgeViagem(s) {
    const map = {
        PLANEJADA: '<span class="badge badge-info">Planejada</span>',
        EM_TRANSITO: '<span class="badge badge-warning">Em Transito</span>',
        ENTREGUE: '<span class="badge badge-success">Entregue</span>',
        CANCELADA: '<span class="badge badge-danger">Cancelada</span>'
    };
    return map[s] || s;
}

// ── Mascaras de input (sugestao do Leonardo) ──────────────────────────

function aplicarMascaraCnpj(valor) {
    const d = valor.replace(/\D/g, '').slice(0, 14);
    return d
        .replace(/^(\d{2})(\d)/, '$1.$2')
        .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
        .replace(/\.(\d{3})(\d)/, '.$1/$2')
        .replace(/(\d{4})(\d)/, '$1-$2');
}

function aplicarMascaraTelefone(valor) {
    const d = valor.replace(/\D/g, '').slice(0, 11);
    if (d.length <= 10) {
        return d
            .replace(/^(\d{2})(\d)/, '($1) $2')
            .replace(/(\d{4})(\d)/, '$1-$2');
    }
    return d
        .replace(/^(\d{2})(\d)/, '($1) $2')
        .replace(/(\d{5})(\d)/, '$1-$2');
}

function inicializarMascaras() {
    document.querySelectorAll('input[data-mask]').forEach(input => {
        if (input.dataset.maskInitialized) return;
        input.dataset.maskInitialized = '1';
        const tipo = input.dataset.mask;
        input.addEventListener('input', e => {
            const v = e.target.value;
            e.target.value = tipo === 'cnpj'
                ? aplicarMascaraCnpj(v)
                : aplicarMascaraTelefone(v);
        });
    });
}

// ── Init ──────────────────────────────────────────────────────────────

window.addEventListener('DOMContentLoaded', () => {
    lucide.createIcons();
    inicializarMascaras();
    const saved = localStorage.getItem('user');
    if (saved) {
        currentUser = JSON.parse(saved);
        showApp();
    }
});
